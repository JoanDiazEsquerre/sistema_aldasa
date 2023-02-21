package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Contrato;


public interface ContratoService {

	Optional<Contrato> findById(Integer id);
	Contrato save(Contrato entity);
	void delete(Contrato entity);
	List<Contrato> findAll();
}
