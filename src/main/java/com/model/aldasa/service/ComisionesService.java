package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Comisiones;

public interface ComisionesService {

	Optional<Comisiones> findById(Integer id);
	Comisiones save(Comisiones entity);
	void delete(Comisiones entity);
	
	
}
