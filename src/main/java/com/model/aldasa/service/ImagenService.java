package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Inventario;

public interface ImagenService {
	
	Optional<Imagen> findById(Integer id);
	Imagen save(Imagen entity);
	void delete(Imagen entity);
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);
	

}
