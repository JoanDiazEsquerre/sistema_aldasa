package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;


public interface ContratoService {

	Optional<Contrato> findById(Integer id);
	Contrato save(Contrato entity);
	void delete(Contrato entity);
	List<Contrato> findAll();
	
	Page<Contrato> findByEstado(boolean status, Pageable pageable);


}
