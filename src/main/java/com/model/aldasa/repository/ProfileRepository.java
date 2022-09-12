package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	List<Profile> findByStatus(boolean status);
}
