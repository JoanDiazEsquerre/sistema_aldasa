package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.DetalleOrdenCompra;
import com.model.aldasa.entity.OrdenCompra;
import com.model.aldasa.entity.Unidad;

public interface DetalleOrdenCompraService {

	Optional<DetalleOrdenCompra> findById(Integer id);
	DetalleOrdenCompra save(DetalleOrdenCompra entity);
	void delete(DetalleOrdenCompra entity);
	
	List<DetalleOrdenCompra> findByOrdenCompraAndEstado(OrdenCompra ordenCompra, boolean estado);

}
