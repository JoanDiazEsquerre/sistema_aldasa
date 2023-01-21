package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;

public interface RequerimientoSeparacionService {
	
	Optional<RequerimientoSeparacion> findById(Integer id);
	RequerimientoSeparacion save(RequerimientoSeparacion entity);
	void delete(RequerimientoSeparacion entity);
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);

	
	
}
