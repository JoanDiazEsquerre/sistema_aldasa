package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;

public interface ProspectionRepository extends PagingAndSortingRepository<Prospection, Integer>{
	
	List<Prospection> findByStatus(String status);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(String originContact, String assesorSurname, String status, Pageable pageable);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatus(String originContact, String assessorSurname,Person personAssessor, String status, Pageable pageable);
	Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatus(String originContact, String assessorSurname,Person personSupervisor, String status, Pageable pageable);
	Prospection findByProspectAndStatus(Prospect prospect,String status);
	

}
