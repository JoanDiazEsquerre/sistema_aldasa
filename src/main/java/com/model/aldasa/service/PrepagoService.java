package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Prepago;

public interface PrepagoService {
	
	Optional<Prepago> findById(Integer id);
	Prepago save(Prepago entity);
	void delete(Prepago entity);
	
	Page<Prepago> findByGeneraDocumento(boolean generaDocumento, Pageable pageable);


}
