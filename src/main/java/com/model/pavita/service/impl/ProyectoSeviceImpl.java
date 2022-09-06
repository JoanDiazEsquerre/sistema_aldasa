package com.model.pavita.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.pavita.entity.Project;
import com.model.pavita.repository.ProyectoRepository;
import com.model.pavita.service.ProyectoService;

@Service("proyectoService")
public class ProyectoSeviceImpl implements ProyectoService{
	
	@Autowired
	private ProyectoRepository proyectoRepository;

	@Override
	public Optional<Project> findById(Integer id) {
		return proyectoRepository.findById(id);
	}

	@Override
	public Project save(Project entity) {
		return proyectoRepository.save(entity);
	}

	@Override
	public void delete(Project entity) {
		proyectoRepository.delete(entity);
		
	}

	@Override
	public List<Project> findByStatus(String status) {
		return proyectoRepository.findByStatus(status);
	}
	
	

}
