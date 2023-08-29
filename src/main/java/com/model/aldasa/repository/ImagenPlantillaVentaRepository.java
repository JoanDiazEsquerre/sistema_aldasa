package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.ImagenPlantillaVenta;

public interface ImagenPlantillaVentaRepository extends PagingAndSortingRepository<ImagenPlantillaVenta, Integer>{

	List<ImagenPlantillaVenta> findByNombreLikeAndEstado(String nombre, boolean estado);

}
