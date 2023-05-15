package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.DocumentoVentaRepository;
import com.model.aldasa.service.DocumentoVentaService;

@Service("documentoVentaService")
public class DocumentoVentaServiceImpl implements DocumentoVentaService{

	@Autowired
	private DocumentoVentaRepository documentoVentaRepository;

	@Override
	public Optional<DocumentoVenta> findById(Integer id) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findById(id);
	}

	@Override
	public DocumentoVenta save(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.save(entity);
	}

	@Override
	public void delete(DocumentoVenta entity) {
		// TODO Auto-generated method stub
		documentoVentaRepository.delete(entity);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, empresa, fechaIni, fechaFin);
	}

	@Override
	public List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionBetween(estado, sucursal, fechaIni, fechaFin);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa,
			Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(estado, empresa, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni,
			Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursalAndFechaEmisionBetween(estado, sucursal, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<DocumentoVenta> findByEstadoAndSucursal(boolean estado, Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstadoAndSucursal(estado, sucursal, pageable);
	}

	
}
