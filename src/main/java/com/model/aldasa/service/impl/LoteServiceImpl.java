package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.repository.LoteRepository; 
import com.model.aldasa.service.LoteService;

@Service("loteService")
public class LoteServiceImpl implements LoteService {

	@Autowired
	private LoteRepository loteRepository;
	
	@Override
	public Lote save(Lote entity) {
		return loteRepository.save(entity);
	}
	
	@Override
	public void delete(Lote entity) {
		loteRepository.delete(entity);
	}
	
	@Override
	public List<Lote> findByStatus(boolean status) {
		return loteRepository.findByStatus(status);
	}
	
	@Override
	public Lote findByNumberLote(String name) {
		return loteRepository.findByNumberLote(name);
	}
	
	@Override
	public  Lote findByNumberLoteException(String name, int idLote) {
		return loteRepository.findByNumberLoteException(name, idLote);
	}


	@Override
	public Optional<Lote> findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
