package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;

public interface DocumentoVentaService {

	Optional<DocumentoVenta> findById(Integer id);
	DocumentoVenta save(DocumentoVenta entity);
	void delete(DocumentoVenta entity);
	
	Page<DocumentoVenta> findByEstado(boolean estado, Pageable pageable);

}
