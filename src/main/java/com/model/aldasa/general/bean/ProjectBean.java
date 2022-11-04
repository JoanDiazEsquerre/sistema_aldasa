package com.model.aldasa.general.bean;


import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.model.aldasa.entity.Project;
import com.model.aldasa.service.ProjectService;

@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	private List<Project> listProject;
	private Project projectSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarProject();
	}
	
	public void listarProject (){
		listProject= projectService.findByStatus(estado);
	}
	
	public void newProject() {
		tituloDialog="NUEVO PROYECTO";
		projectSelected=new Project();
		projectSelected.setStatus(true);
		projectSelected.setName("");
		
	}
	
	public void modifyProject( ) {
		tituloDialog="MODIFICAR PROYECTO";
		
	}
	
	
	public void saveProject() {
		if(projectSelected.getName().equals("") || projectSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre del proyecto."));
			listarProject();
			return ;
		} 
		if (tituloDialog.equals("NUEVO PROYECTO")) {
			Project validarExistencia = projectService.findByName(projectSelected.getName());
			if (validarExistencia == null) {
				projectService.save(projectSelected);
				newProject();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProject();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El proyecto ya existe."));
				listarProject();
			}
		} else {
			Project validarExistencia = projectService.findByNameException(projectSelected.getName(), projectSelected.getId());
			if (validarExistencia == null) {
				projectService.save(projectSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProject();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El proyecto ya existe."));
				listarProject();
			}
		}
		
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public Project getProjectSelected() {
		return projectSelected;
	}
	public void setProjectSelected(Project projectSelected) {
		this.projectSelected = projectSelected;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public void setListProject(List<Project> listProject) {
		this.listProject = listProject;
	}
	public List<Project> getListProject() {
		return listProject;
	}

	

	
	
}
