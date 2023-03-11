package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Cuota;
import com.model.aldasa.repository.CuotaRepository;
import com.model.aldasa.service.CuotaService;

@Service("cuotaService")
public class CuotaServiceImpl implements CuotaService{

	@Autowired
	private  CuotaRepository cuotaRepository;

	@Override
	public Optional<Cuota> findById(Integer id) {
		// TODO Auto-generated method stub
		return cuotaRepository.findById(id);
	}

	@Override
	public Cuota save(Cuota entity) {
		// TODO Auto-generated method stub
		return cuotaRepository.save(entity);
	}

	@Override
	public void delete(Cuota entity) {
		// TODO Auto-generated method stub
		cuotaRepository.delete(entity);
	}

}
