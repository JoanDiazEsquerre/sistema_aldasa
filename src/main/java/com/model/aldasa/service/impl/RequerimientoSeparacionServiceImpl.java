package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.service.RequerimientoSeparacionService;

@Service("requerimientoSeparacionService")
public class RequerimientoSeparacionServiceImpl  implements RequerimientoSeparacionService  {
	
	@Autowired
	private RequerimientoSeparacionRepository requerimientoSeparacionRepository;


	@Override
	public Optional<RequerimientoSeparacion> findById(Integer id) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findById(id);
	}

	@Override
	public RequerimientoSeparacion save(RequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.save(entity);
	}

	@Override
	public void delete(RequerimientoSeparacion entity) {
		// TODO Auto-generated method stub
		requerimientoSeparacionRepository.delete(entity);
	}

}
