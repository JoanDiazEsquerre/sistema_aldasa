package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Project;

public interface ProjectService {
	
	Optional<Project> findById(Integer id);
	Project save(Project proyecto) ;
	void delete(Project proyecto);
	List<Project> findByStatus(String status);

}
