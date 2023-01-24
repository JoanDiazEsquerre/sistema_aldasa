package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.repository.RequerimientoSeparacionRepository;
import com.model.aldasa.service.RequerimientoSeparacionService;

@Service("requerimientoSeparacionService")
public class RequerimientoSeparacionServiceImpl implements RequerimientoSeparacionService {
	
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

	@Override
	public List<RequerimientoSeparacion> findByProspection(Prospection prostection) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findByProspection(prostection);
	}

	@Override
	public Page<RequerimientoSeparacion> findAllByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoSeparacionRepository.findAllByEstado(estado, pageable);
	}


}
