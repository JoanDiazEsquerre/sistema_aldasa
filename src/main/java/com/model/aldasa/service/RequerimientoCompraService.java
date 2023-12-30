package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.RequerimientoCompra;

public interface RequerimientoCompraService {

	Optional<RequerimientoCompra> findById(Integer id);
	RequerimientoCompra save(RequerimientoCompra entity);
	void delete(RequerimientoCompra entity);
	
	Page<RequerimientoCompra> findByEstado(String estado, Pageable pageable);

}
