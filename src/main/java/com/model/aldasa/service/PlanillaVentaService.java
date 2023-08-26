package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.PlanillaVenta;

public interface PlanillaVentaService {

	Optional<PlanillaVenta> findById(Integer id);
	PlanillaVenta save(PlanillaVenta entity);
	void delete(PlanillaVenta entity);
}
