package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.OrdenCompra;
import com.model.aldasa.repository.OrdenCompraRepository;
import com.model.aldasa.service.OrdenCompraService;

@Service("ordenCompraService")
public class OrdenCompraServiceImpl implements OrdenCompraService {

	@Autowired
	private OrdenCompraRepository ordenCompraRepository;

	@Override
	public Optional<OrdenCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return ordenCompraRepository.findById(id);
	}

	@Override
	public OrdenCompra save(OrdenCompra entity) {
		// TODO Auto-generated method stub
		return ordenCompraRepository.save(entity);
	}

	@Override
	public void delete(OrdenCompra entity) {
		// TODO Auto-generated method stub
		ordenCompraRepository.delete(entity);
	}

	@Override
	public Page<OrdenCompra> findByEstado(String estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return ordenCompraRepository.findByEstado(estado, pageable);
	}
}
