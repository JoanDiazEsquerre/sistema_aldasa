package com.model.aldasa.general.bean;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Person;
import com.model.aldasa.service.PersonService;

@Component
@ManagedBean
@SessionScoped
public class PersonBean{

	@Autowired
	private PersonService personService;

//	private List<Person> lstPersons;
	private LazyDataModel<Person> lstPersonsLazy;

	private Person personSelected; 

	private Boolean estado = true;
	private String tituloDialog;

	@PostConstruct
	public void init() {
		listarCliente();
	}

	public void listarCliente() {
//		listarPersonas();
		iniciarLazy();
	}
	
	public void iniciarLazy() {
		lstPersonsLazy = new LazyDataModel<Person>() {
			private List<Person> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Person getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Person person : datasource) {
                    if (person.getId() == intRowKey) {
                        return person;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Person person) {
                return String.valueOf(person.getId());
            }

			@Override
			public List<Person> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				//Aqui capturo cada filtro(Si en caso existe), le pongo % al principiio y al final y reemplazo los espacios por %, para hacer el LIKE
				//Si debageas aqui te vas a dar cuenta como lo captura
				String dni="%"+ (filterBy.get("dni")!=null?filterBy.get("dni").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String names="%"+ (filterBy.get("surnames")!=null?filterBy.get("surnames").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
				Pageable pageable = PageRequest.of(first/pageSize, pageSize);
				//Aqui llamo al servicio que a  su vez llama al repositorio que contiene la sentencia LIKE, 
				//Aqui tu tienes que completar la query, yo solo lo he hecho para dni y nombre a modo de ejemplo
				//Tu deberias preparar el metodo para cada filtro que tengas en la tabla
				Page<Person> pagePerson= personService.findAllByDniLikeAndSurnamesLikeAndStatus(dni, names,estado,pageable);
				setRowCount((int) pagePerson.getTotalElements());
				return datasource = pagePerson.getContent();
			}
		};
	}
	
//	public void listarPersonas() {
//		lstPersons = personService.findByStatus(estado);
//	}

	public void newPerson() {
		personSelected = new Person();
		tituloDialog = "NUEVA PERSONA";
		
		personSelected.setStatus(true);
	}
	
	public void updatePerson() {
		tituloDialog = "MODIFICAR PERSONA";
	}
	

	public boolean validarDatosPersona(Person person) {
		boolean valor = true;
		
		if(person.getDni().equals("") || person.getDni()==null) {
			personSelected.setDni("");
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar DNI."));
//			listarPersonas();
//			return false ;
		}else {
			if(tituloDialog.equals("NUEVA PERSONA")) {
				Person buscarPorDni = personService.findByDni(person.getDni());
				if(buscarPorDni!=null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El DNI ya existe."));
//					listarPersonas();
					return false;
				}
			}else {
				Person buscarPorDni = personService.findByDniException(person.getDni(),person.getId());
				if(buscarPorDni!=null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El DNI ya existe."));
//					listarPersonas();
					return false;
				}
			}
			
		}
		if(person.getSurnames().equals("") || person.getSurnames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Apellidos."));
//			listarPersonas();
			return false ;
		}
		if(person.getNames().equals("") || person.getNames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombres."));
//			listarPersonas();
			return false ;
		}
		if(person.getAddress().equals("") || person.getAddress()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar dirección."));
//			listarPersonas();
			return false ;
		}
		if(person.getPhone().equals("") || person.getPhone()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Teléfono."));
//			listarPersonas();
			return false ;
		}
		
		return valor;
		
	}
	
	public void savePerson() {
		boolean valida = validarDatosPersona(personSelected);
		
		if(valida) {
			Person per = personService.save(personSelected); 
			if(per==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede guardar."));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
//				listarPersonas();
				if(tituloDialog.equals("NUEVA PERSONA")) {
					personSelected=new Person();
				}
			}		
		}
	}
	

	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
//	public List<Person> getLstPersons() {
//		return lstPersons;
//	}
//	public void setLstPersons(List<Person> lstPersons) {
//		this.lstPersons = lstPersons;
//	}
	public Person getPersonSelected() {
		return personSelected;
	}
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public LazyDataModel<Person> getLstPersonsLazy() {
		return lstPersonsLazy;
	}
	public void setLstPersonsLazy(LazyDataModel<Person> lstPersonsLazy) {
		this.lstPersonsLazy = lstPersonsLazy;
	}


	/**/
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
