package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	List<Profile> findByStatus(boolean status);
	Profile findByName(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM profile WHERE name=:name AND id <> :idProfile ")
    Profile findByNameException(String name, int idProfile);
}
