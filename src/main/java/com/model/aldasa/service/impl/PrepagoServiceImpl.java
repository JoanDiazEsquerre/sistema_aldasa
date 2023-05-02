package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Prepago;
import com.model.aldasa.repository.PrepagoRepository;
import com.model.aldasa.service.PrepagoService;

@Service("prepagoService")
public class PrepagoServiceImpl implements PrepagoService{
	
	@Autowired
	private PrepagoRepository prepagoRepository;

	@Override
	public Optional<Prepago> findById(Integer id) {
		// TODO Auto-generated method stub
		return prepagoRepository.findById(id);
	}

	@Override
	public Prepago save(Prepago entity) {
		// TODO Auto-generated method stub
		return prepagoRepository.save(entity);
	}

	@Override
	public void delete(Prepago entity) {
		// TODO Auto-generated method stub
		prepagoRepository.delete(entity);
	}

	@Override
	public Page<Prepago> findByGeneraDocumento(boolean generaDocumento, Pageable pageable) {
		// TODO Auto-generated method stub
		return prepagoRepository.findByGeneraDocumento(generaDocumento, pageable);
	}

}
