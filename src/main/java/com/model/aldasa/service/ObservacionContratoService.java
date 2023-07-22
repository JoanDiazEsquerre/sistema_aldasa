package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.ObservacionContrato;

public interface ObservacionContratoService {
	
	Optional<ObservacionContrato> findById(Integer id);
	ObservacionContrato save(ObservacionContrato entity);
	void delete(ObservacionContrato entity);

}
