package com.model.aldasa.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ProjectService;

import javax.faces.convert.Converter;

@Component
@ManagedBean
@SessionScoped
public class ProyectoBean {
	
	@Autowired
	private ProjectService proyectoService;

	private List<Project> listProyecto;
	
	private Project projectSelected;

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
		projectSelected = new Project();
		
	}

	public ProjectService getProyectoService() {
		return proyectoService;
	}

	public void setProyectoService(ProjectService proyectoService) {
		this.proyectoService = proyectoService;
	}

	public List<Project> getListProyecto() {
		return listProyecto;
	}

	public void setListProyecto(List<Project> listProyecto) {
		this.listProyecto = listProyecto;
	}

	public Project getProjectSelected() {
		return projectSelected;
	}

	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	

	  
	  
	

}
