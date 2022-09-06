package com.model.pavita.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.model.pavita.entity.Project;
import com.model.pavita.entity.Usuario;
import com.model.pavita.service.ProyectoService;

import javax.faces.convert.Converter;

@Component
@ManagedBean
@SessionScoped
public class ProyectoBean {
	
	@Autowired
	private ProyectoService proyectoService;

	private List<Project> listProyecto;
	
	private Project proyectoSelected;

	private String estado = "ACT";
	

	@PostConstruct
	public void init() {

		listarProyectos();
	}

	public void listarProyectos() {
		listProyecto = proyectoService.findByStatus(estado);
		
	}

	public void onRowSelect(SelectEvent<Project> event) {
		// proceso="UPD";
	}
	
	public void newProyecto() {
		
		
		proyectoSelected = new Project();
		
	}

	public ProyectoService getProyectoService() {
		return proyectoService;
	}

	public void setProyectoService(ProyectoService proyectoService) {
		this.proyectoService = proyectoService;
	}

	public List<Project> getListProyecto() {
		return listProyecto;
	}

	public void setListProyecto(List<Project> listProyecto) {
		this.listProyecto = listProyecto;
	}

	public Project getProyectoSelected() {
		return proyectoSelected;
	}

	public void setProyectoSelected(Project proyectoSelected) {
		this.proyectoSelected = proyectoSelected;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	

	  
	  
	

}
