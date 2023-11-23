package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Planilla;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.repository.CuentaBancariaRepository;
import com.model.aldasa.repository.PlanillaRepository;
import com.model.aldasa.repository.VoucherRepository;
import com.model.aldasa.service.PlanillaService;
import com.model.aldasa.service.VoucherService;


@Service("planillaService")
public class PlanillaServiceImpl implements PlanillaService {

	@Autowired
	private PlanillaRepository planillaRepository;

	@Override
	public Optional<Planilla> findById(Integer id) {
		// TODO Auto-generated method stub
		return planillaRepository.findById(id);
	}

	@Override
	public Planilla save(Planilla entity) {
		// TODO Auto-generated method stub
		return planillaRepository.save(entity);
	}

	@Override
	public void delete(Planilla entity) {
		// TODO Auto-generated method stub
		planillaRepository.delete(entity);
	}

	@Override
	public Page<Planilla> findByEstado(boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return planillaRepository.findByEstado(estado, pageable); 
	}

	@Override
	public Planilla findByEstadoAndTemporal(boolean estado, boolean temporal) {
		// TODO Auto-generated method stub
		return planillaRepository.findByEstadoAndTemporal(estado, temporal);
	}

	
	
}
