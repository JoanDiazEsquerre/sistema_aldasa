package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Project;

public interface LoteRepository extends JpaRepository<Lote, Integer> {

	List<Lote> findByStatus(boolean status);
	Lote findByNumberLoteAndManzanaAndProject (String name, Manzana manzana, Project project);
	
	List<Lote> findByProject(Project project);
	
	@Query(nativeQuery = true,value = "SELECT * FROM lote WHERE numberLote=:name AND idManzana=:manzana AND idProject=:project AND id <> :idLote ")
	Lote findByNumberLoteAndManzanaAndProjectException(String name, int manzana, int project, int idLote);
	
	Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndProjectNameLikeAndStatusLike(String numberLote, String nameManzana,String projectName ,String status,Pageable pageable);
	Page<Lote> findAllByNumberLoteLikeAndManzanaNameLikeAndStatusLike(String numberLote,  String nameManzana, String status,Pageable pageable);
}
