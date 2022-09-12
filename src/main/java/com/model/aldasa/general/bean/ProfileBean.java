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
	
	private List<Profile> listProfile;
	
	@PostConstruct
	public void init()  {
		listProfile=profileService.findByStatus(true);
		/*Profile profile1 = new Profile();
		profile1.setName("Admin");
		profile1.setId(1);
		
		Profile profile2 = new Profile();
		profile2.setName("Asesor");
		profile2.setId(2);
		
		listProfile = new ArrayList<>();
		listProfile.add(profile1);
		listProfile.add(profile2);*/
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


	
	
}
