package com.model.aldasa.general.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.service.ProfileService;

@Component
@ManagedBean
@SessionScoped

public class ProfileBean {
	
	@Autowired
	private ProfileService profileService;
	
	private List<Profile> ListProfile;
	private Profile profileSelected;
	
	@PostConstruct
	public void init() {
		ListProfile=profileService.findByStatus(true);
	}
	public void newProfile() {
		profileSelected=new Profile();
		profileSelected.setStatus(true);
		profileSelected.setName("");
	}
	
	public void saveProfile() {
		profileService.save(profileSelected);
		ListProfile=profileService.findByStatus(true);
	}
	
	public ProfileService getProfileService() {
		return profileService;
	}
	
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	public List<Profile> getListProfile() {
		return ListProfile;
	}
	
	public void setListProfile(List<Profile> listProfile) {
		ListProfile = listProfile;
	}
	
	public Profile getProfileSelected() {
		return profileSelected;
	}
	public void setProfileSelected(Profile profileSelected) {
		this.profileSelected = profileSelected;
	}
	
}
