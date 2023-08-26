package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.PlantillaVenta;

public interface PlantillaVentaService {

	Optional<PlantillaVenta> findById(Integer id);
	PlantillaVenta save(PlantillaVenta entity);
	void delete(PlantillaVenta entity);
}
