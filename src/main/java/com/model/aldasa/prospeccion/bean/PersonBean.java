package com.model.aldasa.prospeccion.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Person;
import com.model.aldasa.service.PersonService;

@Component
@ManagedBean
@SessionScoped
public class PersonBean {

	@Autowired
	private PersonService personService;

	private List<Person> lstPersons;

	private Person personSelected;

	private String estado = "ACT";
	private String tituloDialog;
	private String mensaje;

	@PostConstruct
	public void init() {
		listarCliente();
	}

	public void listarCliente() {
		lstPersons = personService.findByStatus(estado);

	}

	public void newPerson() {
		tituloDialog = "NUEVA PERSONA";
		mensaje="";
		personSelected = new Person();
		personSelected.setStatus("ACT");
	}
	
	public void updatePerson() {
		tituloDialog = "MOFIFICAR PERSONA";
		mensaje="";
	}
	
	public void savePerson() {
		
	    personService.save(personSelected);
	    lstPersons = personService.findByStatus(estado);
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

	/**/
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
}
