package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Prospection;

public interface LoteService {
 
	Optional<Lote> findById(Integer id);
	Lote save(Lote lote);
	void delete(Lote lote);
	List<Lote> findByStatus(boolean status);
	List<Lote> findByProject(Project project);
	Lote findByNumberLoteAndManzanaAndProject (String name, Manzana manzana, Project project);
	Lote findByNumberLoteAndManzanaAndProjectException(String name, int manzana, int project, int idLote);
	
	Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(String numberLote, String nameManzana,String projectName ,String status,Pageable pageable);
	Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(String numberLote,  String nameManzana, String status,Pageable pageable);

}
