package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.ImagenPlantillaVenta;

public interface ImagenPlantillaVentaService {

	Optional<ImagenPlantillaVenta> findById(Integer id);
	ImagenPlantillaVenta save(ImagenPlantillaVenta entity);
	void delete(ImagenPlantillaVenta entity);
}
