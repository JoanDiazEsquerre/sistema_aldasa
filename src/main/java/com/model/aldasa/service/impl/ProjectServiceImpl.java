package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Project;
import com.model.aldasa.repository.ProjectRepository; 
import com.model.aldasa.service.ProjectService;

@Service("projectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public Project save(Project entity) {
		return projectRepository.save(entity);
	}
	
	@Override
	public void delete(Project entity) {
		projectRepository.delete(entity);
	}
	
	@Override
	public List<Project> findByStatus(boolean status) {
		return projectRepository.findByStatus(status);
	}
	
	@Override
	public Project findByName(String name) {
		return projectRepository.findByName(name);
	}
	
	@Override
	public  Project findByNameException(String name, int idProject) {
		return projectRepository.findByNameException(name, idProject);
	}


	@Override
	public Optional<Project> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
