package com.model.aldasa.general.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.model.aldasa.entity.Profile;
import com.model.aldasa.service.ProfileService;

@ManagedBean
@ViewScoped
public class ProfileBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{profileService}")
	private ProfileService profileService;
	
	private List<Profile> listProfile;
	private Profile profileSelected;
	private boolean estado = true;
	
	private String tituloDialog;
	
	@PostConstruct
	public void init() {
		listarProfiles();
	}
	public void listarProfiles (){
		listProfile=profileService.findByStatus(estado);
	}
	public void newProfile() {
		tituloDialog="NUEVO PERFIL";
		profileSelected=new Profile();
		profileSelected.setStatus(true);
		profileSelected.setName("");
		
	}
	
	public void modifyProfile( ) {
		tituloDialog="MODIFICAR PERFIL";
		
	}
	
	
	public void saveProfile() {
		if(profileSelected.getName().equals("") || profileSelected.getName()==null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ingresar Nombre del perfil."));
			listarProfiles();
			return ;
		} 
		if (tituloDialog.equals("NUEVO PERFIL")) {
			Profile validarExistencia = profileService.findByName(profileSelected.getName());
			if (validarExistencia == null) {
				profileService.save(profileSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProfiles();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El perfil ya existe."));
				listarProfiles();
			}
		} else {
			Profile validarExistencia = profileService.findByNameException(profileSelected.getName(), profileSelected.getId());
			if (validarExistencia == null) {
				profileService.save(profileSelected);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se guardo correctamente."));
				listarProfiles();
			} else { 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El perfil ya existe."));
				listarProfiles();
			}
		}
		
	}

	
	public ProfileService getProfileService() {
		return profileService;
	}
	
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	
	public List<Profile> getListProfile() {
		return listProfile;
	}
	public void setListProfile(List<Profile> listProfile) {
		this.listProfile = listProfile;
	}
	public Profile getProfileSelected() {
		return profileSelected;
	}
	public void setProfileSelected(Profile profileSelected) {
		this.profileSelected = profileSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
}
