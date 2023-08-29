package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Sucursal;
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

	@Override
	public Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(
			String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(estado, prospect, project, manzana, lote, sucursal, pageable);
	}

	@Override
	public Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonSupervisor(
			String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal,
			Person supervisor, Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonSupervisor(estado, prospect, project, manzana, lote, sucursal, supervisor, pageable);
	}

	@Override
	public Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonAssessor(
			String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal,
			Person asesor, Pageable pageable) {
		// TODO Auto-generated method stub
		return plantillaVentaRepository.findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonAssessor(estado, prospect, project, manzana, lote, sucursal, asesor, pageable);
	}

	
}
