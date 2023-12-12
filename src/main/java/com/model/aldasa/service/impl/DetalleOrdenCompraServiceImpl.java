package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleOrdenCompra;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.DetalleOrdenCompraRepository;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.service.DetalleOrdenCompraService;

@Service("detalleOrdenCompraService")
public class DetalleOrdenCompraServiceImpl implements DetalleOrdenCompraService {

	@Autowired
	private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

	@Override
	public Optional<DetalleOrdenCompra> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleOrdenCompraRepository.findById(id);
	}

	@Override
	public DetalleOrdenCompra save(DetalleOrdenCompra entity) {
		// TODO Auto-generated method stub
		return detalleOrdenCompraRepository.save(entity);
	}

	@Override
	public void delete(DetalleOrdenCompra entity) {
		// TODO Auto-generated method stub
		detalleOrdenCompraRepository.delete(entity);
	}
}
