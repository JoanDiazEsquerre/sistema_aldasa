package com.model.aldasa.prospeccion.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
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
import com.model.aldasa.util.Perfiles;

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
	
	private String username,tituloDialog;
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
				
				if(usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
					// si es Administrador
					pagePerson= prospectService.findAllByPersonDniLike(dni,pageable); 
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
					// si es asesor
					pagePerson= prospectService.findAllByPersonDniLikeAndPersonAssessor(dni, usuarioLogin.getPerson(), pageable); 
				}else if(usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())){
					// Es supervisor
					pagePerson= prospectService.findAllByPersonDniLikeAndPersonSupervisor(dni,usuarioLogin.getPerson(),pageable); 
				}
				
//				pagePerson= prospectService.findAllByPersonDniLike(dni,pageable); 
				
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}
	
	public void newPerson() {
		personNew = new Person();
		personNew.setStatus(true);
		tituloDialog = "NUEVA PERSONA";
	}
	
	public void updatePerson() {
		tituloDialog = "MODIFICAR PERSONA";
		personNew=prospectSelected.getPerson();
	}
	

	public void completar() {
		if(tituloDialog.equals("NUEVA PERSONA")) {
			Person buscarPorDni = personService.findByDni(personNew.getDni());
			if(buscarPorDni!=null) {
				personNew.setNames(buscarPorDni.getNames());
				personNew.setSurnames(buscarPorDni.getSurnames());
				personNew.setAddress(buscarPorDni.getAddress());
//				personNew.setPhone(buscarPorDni.getPhone());
//				personNew.setCellphone(buscarPorDni.getCellphone());
				personNew.setStatus(true);
				personNew.setCivilStatus(buscarPorDni.getCivilStatus());
				personNew.setOccupation(buscarPorDni.getOccupation());
				personNew.setGender(buscarPorDni.getGender());
				personNew.setMonthEntry(buscarPorDni.getMonthEntry());
			}	
		}
		
	}
	
	public void savePerson() {		
		if(tituloDialog.equals("NUEVA PERSONA")) {
			if(personNew.getSurnames().equals("") || personNew.getSurnames()==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Apellidos."));
				return;
			}
			if(personNew.getNames().equals("") || personNew.getNames()==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombres."));
				return;
			}
			
//			if (!personNew.getDni().equals("") || personNew.getDni() != null) {
//				Person buscarPersona = personService.findByDni(personNew.getDni());
//				if (buscarPersona != null) {
//					personNew.setId(buscarPersona.getId());
//					Prospect buscarProspecto = prospectService.findByPerson(buscarPersona);
//					if (buscarProspecto != null) {
//						Date fechaRest = sumaRestarFecha(buscarProspecto.getDateBlock(), 180);
//						if(fechaRest.after(new Date())) {
//							if (buscarProspecto.getPersonAssessor() != null) {
//								Usuario buscarInactivo = usuarioService.findByPerson(buscarProspecto.getPersonAssessor());
//								FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El prospecto está a cargo por el asesor "+ buscarProspecto.getPersonAssessor().getSurnames() + " "+ buscarProspecto.getPersonAssessor().getNames()));
//								return;
//							} else if (buscarProspecto.getPersonSupervisor() != null) {
//								FacesContext.getCurrentInstance().addMessage(null,
//										new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error","El prospecto está a cargo por el supervisor "+ buscarProspecto.getPersonSupervisor().getSurnames() + " "+ buscarProspecto.getPersonSupervisor().getNames()));
//								return;
//							} 
//						}else {
//							if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
//								buscarProspecto.setPersonAssessor(null);
//							} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
//								buscarProspecto.setPersonAssessor(usuarioLogin.getPerson());
//								buscarProspecto.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
//							} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
//								buscarProspecto.setPersonSupervisor(usuarioLogin.getPerson());
//							}
//							
//						
//							personService.save(personNew);
//							buscarProspecto.setDateBlock(new Date());
// 							prospectService.save(buscarProspecto);
//							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info", "El prospecto se guardó correctamente"));
//							personNew = new Person();
//							personNew.setStatus(true);
//							return;
//						}
//					}
//				}
//			}
			
			Prospect prospectNew = new Prospect();
			
			if (!personNew.getDni().equals("") || personNew.getDni() != null) {
				Person buscarPersona = personService.findByDni(personNew.getDni());
				if (buscarPersona != null) {
					personNew.setId(buscarPersona.getId());
					Prospect buscarProspecto = prospectService.findByPerson(buscarPersona);
					if (buscarProspecto != null) {
						prospectNew=buscarProspecto;
					}
				}
			}
			
			
			Person person =personService.save(personNew);
			
			prospectNew.setDateBlock(new Date());
			prospectNew.setPerson(person);
			if (usuarioLogin.getProfile().getName().equals(Perfiles.ADMINISTRADOR.getName())) {
				prospectNew.setPersonAssessor(null);
			} else if (usuarioLogin.getProfile().getName().equals(Perfiles.ASESOR.getName())) {
				prospectNew.setPersonAssessor(usuarioLogin.getPerson());
				prospectNew.setPersonSupervisor(usuarioLogin.getTeam().getPersonSupervisor());
			} else if (usuarioLogin.getProfile().getName().equals(Perfiles.SUPERVISOR.getName())) {
				prospectNew.setPersonSupervisor(usuarioLogin.getPerson());
			}
			
			prospectService.save(prospectNew);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info", "El prospecto se guardó correctamente"));
			personNew = new Person();
			personNew.setStatus(true);
			
			
		}else {
			Person buscarPorDni = personService.findByDniException(personNew.getDni(),personNew.getId());
			if(buscarPorDni!=null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El DNI ya existe."));
			}else {
				Person per = personService.save(personNew); 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info", "El prospecto se guardó correctamente"));
			}
		}
		
	}
	
	public Date sumaRestarFecha(Date fecha, int sumaresta){
        Calendar calendar = Calendar.getInstance();
        try{

            calendar.setTime(fecha);
            
            calendar.add(Calendar.DAY_OF_WEEK, sumaresta);
     
        }
        catch(Exception e)
        {
            System.out.println("Error:\n" + e);
        }
        return calendar.getTime();
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

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	
	
	
}
