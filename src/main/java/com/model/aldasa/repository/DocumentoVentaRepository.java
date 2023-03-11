package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DocumentoVenta;

public interface DocumentoVentaRepository  extends JpaRepository<DocumentoVenta, Integer>   {
	
	Page<DocumentoVenta> findByEstado(boolean estado, Pageable pageable);


}
