package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Contrato;

public interface ContratoRepository extends PagingAndSortingRepository<Contrato, Integer> {
	
	Page<Contrato> findByEstado(boolean status, Pageable pageable);


}
