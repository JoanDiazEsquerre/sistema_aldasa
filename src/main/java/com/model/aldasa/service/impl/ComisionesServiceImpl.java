package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.repository.ComisionesRepository;
import com.model.aldasa.service.ComisionesService;

@Service("comisionesService")
public class ComisionesServiceImpl implements ComisionesService  {

	
	@Autowired
	private ComisionesRepository comisionesRepository;

	@Override
	public Optional<Comisiones> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionesRepository.findById(id);
	}

	@Override
	public Comisiones save(Comisiones entity) {
		// TODO Auto-generated method stub
		return comisionesRepository.save(entity);
	}

	@Override
	public void delete(Comisiones entity) {
		// TODO Auto-generated method stub
		comisionesRepository.delete(entity);
	}

	

}
