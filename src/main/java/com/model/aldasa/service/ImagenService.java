package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Imagen;

public interface ImagenService {
	
	Optional<Imagen> findById(Integer id);
	Imagen save(Imagen entity);
	void delete(Imagen entity);
	

}
