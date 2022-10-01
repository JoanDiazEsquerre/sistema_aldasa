package com.model.aldasa.prospeccion.bean;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProspectService;
import com.model.aldasa.service.UsuarioService;

@Named
@Component
@ManagedBean
@SessionScoped
public class ProspectoBean {
	
	@Inject
	private NavegacionBean navegacionBean;
	
	@Autowired
	private ProspectService prospectService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PersonService personService;
	
	private LazyDataModel<Prospect> lstProspectLazy;
	private Prospect prospectSelected;
	private Person personNew;
	
	private String username;;
	private Usuario usuarioLogin = new Usuario();

	@PostConstruct
	public void init() {
		iniciarLazy();
	}
	
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());
	}
	
	public void iniciarLazy() {
		lstProspectLazy = new LazyDataModel<Prospect>() {
			private List<Prospect> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Prospect getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Prospect prospect : datasource) {
                    if (prospect.getId() == intRowKey) {
                        return prospect;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Prospect prospect) {
                return String.valueOf(prospect.getId());
            }

			@Override
			public List<Prospect> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String dni="%"+ (filterBy.get("person.dni")!=null?filterBy.get("person.dni").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
//				String assessorSurnames="%"+ (filterBy.get("assessor.surnames")!=null?filterBy.get("assessor.surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				Pageable pageable = PageRequest.of(first/pageSize, pageSize);
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				
				Page<Prospect> pagePerson = null; 
				
				if(usuarioLogin.getProfile().getName().equals(Perfiles.Administrador.toString())) {
					// si es Administrador
					pagePerson= prospectService.findAllByPersonDniLike(dni,pageable); 
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.Asesor.toString())) {
					// si es asesor
					pagePerson= prospectService.findAllByPersonDniLikeAndUsuarioAssessor(dni, usuarioLogin, pageable); 
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.Supervisor.toString())){
					// Es supervisor
					pagePerson= prospectService.findAllByPersonDniLikeAndUsuarioAssessorTeamPersonSupervisor(dni,usuarioLogin.getPerson(),pageable); 
				}
				
//				pagePerson= prospectService.findAllByPersonDniLike(dni,pageable); 
				
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}
	
	enum Perfiles{
	    Administrador, 
	    Asesor, 
	    Supervisor;
	}
	
	private void newPerson() {
		personNew = new Person();
		personNew.setStatus(true); 
	}
	
	public boolean validarDatosPersona(Person person) {
		boolean valor = true;
		
		
		
		if(person.getAddress().equals("") || person.getAddress()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar dirección."));
			return false ;
		}
		if(person.getPhone().equals("") || person.getPhone()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Teléfono."));
			return false ;
		}
		
		return valor;
		
	}
	
	public void savePerson() {
		if(personNew.getDni().equals("") || personNew.getDni()==null) {
			personNew.setDni(null);

		}else {
			Person buscarPorDni = personService.findByDni(personNew.getDni());
			if(buscarPorDni!=null) {
				Prospect buscarProspecto = prospectService.findByPerson(buscarPorDni);
				if(buscarProspecto.getUsuarioAssessor() != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El prospecto esta en seguimiento por el asesor " + buscarProspecto.getUsuarioAssessor().getPerson().getSurnames()+" "+ buscarProspecto.getUsuarioAssessor().getPerson().getNames()));
					return;
				}
			}
		}
		
		if(personNew.getSurnames().equals("") || personNew.getSurnames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Apellidos."));
			return;
		}
		if(personNew.getNames().equals("") || personNew.getNames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombres."));
			return;
		}
		
		
//		if(valida) {
//			Person per = personService.save(personSelected); 
//			if(per==null) {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede guardar."));
//			}else {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
//				if(tituloDialog.equals("NUEVA PERSONA")) {
//					personSelected=new Person();
//				}
//			}		
//		}
	}
	
	
	public String getUsername() {
		if(SecurityContextHolder.getContext().getAuthentication()!=null) {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    username = ((UserDetails)principal).getUsername();
		}
		return username;
	}
	public ProspectService getProspectService() {
		return prospectService;
	}
	public void setProspectService(ProspectService prospectService) {
		this.prospectService = prospectService;
	}
	public LazyDataModel<Prospect> getLstProspectLazy() {
		return lstProspectLazy;
	}
	public void setLstProspectLazy(LazyDataModel<Prospect> lstProspectLazy) {
		this.lstProspectLazy = lstProspectLazy;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	public Prospect getProspectSelected() {
		return prospectSelected;
	}
	public void setProspectSelected(Prospect prospectSelected) {
		this.prospectSelected = prospectSelected;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public Person getPersonNew() {
		return personNew;
	}
	public void setPersonNew(Person personNew) {
		this.personNew = personNew;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	
	
	
}
