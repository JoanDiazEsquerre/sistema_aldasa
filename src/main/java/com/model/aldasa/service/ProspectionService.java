package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Prospection;

public interface ProspectionService {
	
	Optional<Prospection> findById(Integer id);
	Prospection save(Prospection prospection);
	void delete(Prospection prospection);
	List<Prospection> findByStatus(String status);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(String originContact, String assessorSurname, String status, Pageable pageable);
	Prospection findByProspectPersonIdAndStatus(int idPerson,String status);

}
