package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Producto;

public interface ProductoService {

	Optional<Producto> findById(Integer id);
	Producto save(Producto entity);
	void delete(Producto entity);
	
	Producto findByEstadoAndTipoProducto (boolean estado, String tipoProducto);

} 
