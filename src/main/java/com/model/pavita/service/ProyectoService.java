package com.model.pavita.service;

import java.util.List;
import java.util.Optional;

import com.model.pavita.entity.Project;

public interface ProyectoService {
	
	Optional<Project> findById(Integer id);
	Project save(Project proyecto) ;
	void delete(Project proyecto);
	List<Project> findByStatus(String status);

}
