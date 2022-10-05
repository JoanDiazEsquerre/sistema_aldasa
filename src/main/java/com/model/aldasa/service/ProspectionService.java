package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;

public interface ProspectionService {
	
	Optional<Prospection> findById(Integer id);
	Prospection save(Prospection prospection);
	void delete(Prospection prospection);
	List<Prospection> findByStatus(String status);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(String originContact, String assessorSurname, String status, Pageable pageable);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatus(String originContact, String assessorSurname,Person personAssessor, String status, Pageable pageable);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatus(String originContact, String assessorSurname,Person personSupervisor, String status, Pageable pageable);
	Prospection findByProspectAndStatus(Prospect prospect,String status);

}
