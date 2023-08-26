package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.service.PlantillaVentaService;

@Service("plantillaVentaService")
public class PlantillaVentaServiceImpl implements PlantillaVentaService{

	@Autowired
	private PlantillaVentaRepository plantillaVentaRepository;

	@Override
	public Optional<PlantillaVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findById(id);
	}

	@Override
	public PlantillaVenta save(PlantillaVenta entity) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.save(entity);
	}

	@Override
	public void delete(PlantillaVenta entity) {
		// TODO Auto-generated method stub
		plantillaVentaRepository.delete(entity);
	}
}
