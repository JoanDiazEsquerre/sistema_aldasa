package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Imagen;

public interface ImagenRepository extends PagingAndSortingRepository<Imagen, Integer>{
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);


}
