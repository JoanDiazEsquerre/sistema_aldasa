package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;
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
	public Lote findByNumberLoteAndManzanaAndProject (String name, Manzana manzana, Project project) {
		return loteRepository.findByNumberLoteAndManzanaAndProject(name, manzana, project);
	}
	
	@Override
	public Lote findByNumberLoteAndManzanaAndProjectException(String name, int manzana, int project, int idLote) {
		return loteRepository.findByNumberLoteAndManzanaAndProjectException(name, manzana, project, idLote);
	}


	@Override
	public Optional<Lote> findById(Integer id) {
		// TODO Auto-generated method stub
		return loteRepository.findById(id);
	}

	@Override
	public Page<Lote> findAllByNumberLoteLikeAndProjectNameLikeAndStatus(String numberLote, String projectName,String status,Pageable pageable) {
		// TODO Auto-generated method stub
		return loteRepository.findAllByNumberLoteLikeAndProjectNameLikeAndStatus(numberLote,projectName,status, pageable);
	}

	@Override
	public Page<Lote> findAllByNumberLoteLikeAndStatus(String numberLote, String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return loteRepository.findAllByNumberLoteLikeAndStatus(numberLote, status, pageable);
	}
	
}
