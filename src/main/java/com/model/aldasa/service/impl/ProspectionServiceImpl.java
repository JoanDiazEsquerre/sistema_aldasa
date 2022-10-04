package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.repository.ProspectionRepository;
import com.model.aldasa.service.ProspectionService;

@Service("prospectionService")
public class ProspectionServiceImpl implements ProspectionService {
	
	@Autowired
	private ProspectionRepository prospectionRepository;

	@Override
	public Optional<Prospection> findById(Integer id) {
		// TODO Auto-generated method stub
		return prospectionRepository.findById(id);
	}

	@Override
	public Prospection save(Prospection prospection) {
		// TODO Auto-generated method stub
		return prospectionRepository.save(prospection);
	}

	@Override
	public void delete(Prospection prospection) {
		// TODO Auto-generated method stub
		prospectionRepository.delete(prospection);
	}

	@Override
	public List<Prospection> findByStatus(String status) {
		// TODO Auto-generated method stub
		return prospectionRepository.findByStatus(status);
	}

	@Override
	public Page<Prospection> findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(String originContact, String assessorSurname, String status, Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectionRepository.findAllByOriginContactLikeAndPersonAssessorSurnamesLikeAndStatus(originContact,assessorSurname, status, pageable);
	}
	
	@Override
	public Prospection findByProspectPersonIdAndStatus(int idPerson,String status) {
		// TODO Auto-generated method stub
		return prospectionRepository.findByProspectPersonIdAndStatus(idPerson,status);
	}

}
