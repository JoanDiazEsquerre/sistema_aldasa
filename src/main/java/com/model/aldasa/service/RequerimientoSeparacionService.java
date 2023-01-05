package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.RequerimientoSeparacion;

public interface RequerimientoSeparacionService {

	Optional<RequerimientoSeparacion> findById(Integer id);
	RequerimientoSeparacion save(RequerimientoSeparacion entity);
	void delete(RequerimientoSeparacion entity);
}
