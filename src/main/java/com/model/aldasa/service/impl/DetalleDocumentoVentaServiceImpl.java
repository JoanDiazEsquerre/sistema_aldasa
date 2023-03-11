package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.repository. DetalleDocumentoVentaRepository;
import com.model.aldasa.service.DetalleDocumentoVentaService;

@Service("detalleDocumentoVentaService")
public class DetalleDocumentoVentaServiceImpl implements DetalleDocumentoVentaService{

	@Autowired
	private  DetalleDocumentoVentaRepository  detalleDocumentoVentaRepository;

	@Override
	public Optional<DetalleDocumentoVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.findById(id);
	}

	@Override
	public DetalleDocumentoVenta save(DetalleDocumentoVenta entity) {
		// TODO Auto-generated method stub
		return detalleDocumentoVentaRepository.save(entity);
	}

	@Override
	public void delete(DetalleDocumentoVenta entity) {
		// TODO Auto-generated method stub
		detalleDocumentoVentaRepository.delete(entity);
	}
}
