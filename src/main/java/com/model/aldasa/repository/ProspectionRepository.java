package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Prospection;

public interface ProspectionRepository extends PagingAndSortingRepository<Prospection, Integer>{
	
	List<Prospection> findByStatus(String status);
	Page<Prospection> findAllByOriginContactLikeAndAssessorSurnamesLikeAndStatus(String originContact, String assesorSurname, String status, Pageable pageable);
	Prospection findByPersonIdAndStatus(int idPerson,String status);
	

}
