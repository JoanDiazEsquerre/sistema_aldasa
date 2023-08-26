package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.PlanillaVenta;
import com.model.aldasa.repository.PlanillaVentaRepository;
import com.model.aldasa.service.PlanillaVentaService;

@Service("planillaVentaService")
public class PlanillaVentaServiceImpl implements PlanillaVentaService{

	@Autowired
	private PlanillaVentaRepository planillaVentaRepository;

	@Override
	public Optional<PlanillaVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return planillaVentaRepository.findById(id);
	}

	@Override
	public PlanillaVenta save(PlanillaVenta entity) {
		// TODO Auto-generated method stub
		return planillaVentaRepository.save(entity);
	}

	@Override
	public void delete(PlanillaVenta entity) {
		// TODO Auto-generated method stub
		planillaVentaRepository.delete(entity);
	}
}
