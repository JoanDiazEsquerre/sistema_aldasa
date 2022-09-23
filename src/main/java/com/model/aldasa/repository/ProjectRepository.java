package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

	List<Project> findByStatus(boolean status);
	Project findByName(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM project WHERE name=:name AND id <> :idProject ")
    Project findByNameException(String name, int idProject);
}
