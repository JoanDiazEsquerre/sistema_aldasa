package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Distribucion;

public interface DistribucionService {
	
	Optional<Distribucion> findById(Integer id);
	Distribucion save( Distribucion entity);
	void delete( Distribucion entity);

}
