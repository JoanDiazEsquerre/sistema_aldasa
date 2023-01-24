package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;

public interface RequerimientoSeparacionService {
	
	Optional<RequerimientoSeparacion> findById(Integer id);
	RequerimientoSeparacion save(RequerimientoSeparacion entity);
	void delete(RequerimientoSeparacion entity);
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);
	
	Page<RequerimientoSeparacion> findAllByEstado(String estado, Pageable pageable);


	
	
}
