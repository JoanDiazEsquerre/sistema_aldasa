package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.repository.ProspectionRepository;
import com.model.aldasa.service.ProspectionService;

@Service("prospectionService")
public class ProspectionServiceImpl implements ProspectionService {
	
	@Autowired
	private ProspectionRepository prospectionRepository;

	@Override
	public Optional<Prospection> findById(Integer id) {
		return prospectionRepository.findById(id);
	}

	@Override
	public Prospection save(Prospection prospection) {
		return prospectionRepository.save(prospection);
	}

	@Override
	public void delete(Prospection prospection) {
		prospectionRepository.delete(prospection);
	}

	@Override
	public List<Prospection> findByStatus(String status) {
		return prospectionRepository.findByStatus(status);
	}

	@Override
	public Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(String originContact, String assessorSurname, String status, Pageable pageable) {
		return prospectionRepository.findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(originContact,assessorSurname, status, pageable);
	}
	
	@Override
	public Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatus(
			String originContact, String assessorSurname, Person personAssessor, String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectionRepository.findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonAssessorAndStatus(originContact, assessorSurname, personAssessor, status, pageable);
	}

	@Override
	public Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatus(
			String originContact, String assessorSurname, Person personSupervisor, String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectionRepository.findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndPersonSupervisorAndStatus(originContact, assessorSurname, personSupervisor, status, pageable);
	}
	
	@Override
	public Prospection findByProspectAndStatus(Prospect prospect,String status) {
		return prospectionRepository.findByProspectAndStatus(prospect, status);
	}

	@Override
	public List<Prospection> findByProspect(Prospect prospect) {
		// TODO Auto-generated method stub
		return prospectionRepository.findByProspect(prospect);
	}

	

}
