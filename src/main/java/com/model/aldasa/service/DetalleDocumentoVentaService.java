package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity. DetalleDocumentoVenta;

public interface DetalleDocumentoVentaService {

	Optional< DetalleDocumentoVenta> findById(Integer id);
	DetalleDocumentoVenta save( DetalleDocumentoVenta entity);
	void delete( DetalleDocumentoVenta entity);
}
