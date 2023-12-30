package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.RequerimientoCompra;
import com.model.aldasa.repository.RequerimientoCompraRepository;
import com.model.aldasa.service.RequerimientoCompraService;

@Service("requerimientoCompraService")
public class RequerimientoCompraServiceImpl implements RequerimientoCompraService {

	@Autowired
	private RequerimientoCompraRepository requerimientoCompraRepository;

	@Override
	public Optional<RequerimientoCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.findById(id);
	}

	@Override
	public RequerimientoCompra save(RequerimientoCompra entity) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.save(entity);
	}

	@Override
	public void delete(RequerimientoCompra entity) {
		// TODO Auto-generated method stub
		requerimientoCompraRepository.delete(entity);
	}

	@Override
	public Page<RequerimientoCompra> findByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return requerimientoCompraRepository.findByEstado(estado, pageable);
	}
}
