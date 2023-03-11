package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.DocumentoVenta;
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
	public Page<DocumentoVenta> findByEstado(boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return documentoVentaRepository.findByEstado(estado, pageable);
	}

}
