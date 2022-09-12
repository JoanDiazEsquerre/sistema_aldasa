package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Profile;
import com.model.aldasa.repository.ProfileRepository; 
import com.model.aldasa.service.ProfileService;

@Service("profileService")
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Override
	public Optional<Profile> findBy(Integer id) {
		return profileRepository.findById(id);
	}

	
	@Override
	public Profile save(Profile entity) {
		return profileRepository.save(entity);
	}
	
	@Override
	public void delete(Profile entity) {
		profileRepository.delete(entity);
	}
	
	@Override
	public List<Profile> findByStatus(boolean status) {
		return profileRepository.findByStatus(status);
	}

	
}
