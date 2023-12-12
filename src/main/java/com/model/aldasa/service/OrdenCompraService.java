package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.OrdenCompra;

public interface OrdenCompraService {

	Optional<OrdenCompra> findById(Integer id);
	OrdenCompra save(OrdenCompra entity);
	void delete(OrdenCompra entity);
	
	Page<OrdenCompra> findByEstado(String estado, Pageable pageable);

}
