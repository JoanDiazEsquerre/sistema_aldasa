package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.DetalleOrdenCompra;

public interface DetalleOrdenCompraService {

	Optional<DetalleOrdenCompra> findById(Integer id);
	DetalleOrdenCompra save(DetalleOrdenCompra entity);
	void delete(DetalleOrdenCompra entity);
}
