package com.model.aldasa.prospeccion.bean;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.PersonService;

@Component
@ManagedBean
@SessionScoped
public class PersonBean{

	@Autowired
	private PersonService personService;

	private List<Person> lstPersons;
	private LazyDataModel<Person> lstPersonsLazy;

	private Person personSelected; 

	private Boolean estado = true;
	private String tituloDialog;

	@PostConstruct
	public void init() {
		listarCliente();
	}

	public void listarCliente() {
		listarPersonas();
//		iniciarLazy();
	}
	
//	public void iniciarLazy() {
//		lstPersonsLazy = new LazyDataModel<Person>() {
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//			@Override
//			public List<Person> load(int first, int pageSize, Map<String, SortMeta> sortBy,
//					Map<String, FilterMeta> filterBy) {
//				
//				Pageable firstPageWithTwoElements = PageRequest.of(0, 3);
//				return personService.findByStatus(estado,firstPageWithTwoElements);
//			}
//		};
//	}
	
	public void listarPersonas() {
		lstPersons = personService.findByStatus(estado);
	}

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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar DNI."));
			return false ;
		}
		if(person.getSurnames().equals("") || person.getSurnames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Apellidos."));
			return false ;
		}
		if(person.getNames().equals("") || person.getNames()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta ingresar Nombres."));
			return false ;
		}
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
		boolean valida = validarDatosPersona(personSelected);
		
		if(valida) {
			Person per = personService.save(personSelected); 
			if(per==null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede guardar."));
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardó correctamente."));
				listarPersonas();
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
	public List<Person> getLstPersons() {
		return lstPersons;
	}
	public void setLstPersons(List<Person> lstPersons) {
		this.lstPersons = lstPersons;
	}
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
