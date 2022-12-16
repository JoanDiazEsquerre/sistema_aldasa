package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
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
	public List<Lote> findById(int id) {
		return loteRepository.findById(id);
	}

	@Override
	public Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(String numberLote, String nameManzana,String projectName ,String status,Pageable pageable) {
		return loteRepository.findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(numberLote, nameManzana, projectName, status, pageable);
	}

	@Override
	public Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(String numberLote,  String nameManzana, String status,Pageable pageable){
		return loteRepository.findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(numberLote, nameManzana, status, pageable);
	}

	@Override
	public List<Lote> findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(Project project, Manzana manzana, String status) {
		return loteRepository.findByProjectAndManzanaAndStatusLikeOrderByManzanaNameAscNumberLoteAsc(project, manzana,status);
	}

	@Override
	public Page<Lote> findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween(
			String status, String idPersonSupervisor, String idPersonAsesor, Date fechaIni, Date fechaFin,
			Pageable pageable) {
		return loteRepository.findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween(status, idPersonSupervisor, idPersonAsesor, fechaIni, fechaFin, pageable); 
	}

	@Override
	public List<Lote> findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(String Status, Person personSupervisor,String dniAsesor,
			Date fechaIni, Date fechaFin) {
		return loteRepository.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(Status, personSupervisor, dniAsesor,fechaIni, fechaFin);
	} 
	
}
