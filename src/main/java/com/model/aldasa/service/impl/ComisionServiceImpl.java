package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.repository.ComisionRepository;
import com.model.aldasa.service.ComisionService;

@Service("comisionService")
public class ComisionServiceImpl  implements ComisionService {
	
	@Autowired
	private ComisionRepository comisionRepository;

	@Override
	public Optional<Comision> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionRepository.findById(id);
	}

	@Override
	public Comision save(Comision entity) {
		// TODO Auto-generated method stub
		return comisionRepository.save(entity);
	}

	@Override
	public void delete(Comision entity) {
		// TODO Auto-generated method stub
		comisionRepository.delete(entity);
	}
	
	@Override
	public List<Comision> findAll() {
		// TODO Auto-generated method stub
		return comisionRepository.findAll();
	}

	@Override
	public Page<Comision> findByEstadoAndFechaIniBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionRepository.findByEstadoAndFechaIniBetween(estado, fechaIni, fechaFin, pageable); 
	}

	

	

}
