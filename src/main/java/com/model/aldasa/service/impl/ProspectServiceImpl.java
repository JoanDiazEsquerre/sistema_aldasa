package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.repository.ProspectRepository;
import com.model.aldasa.service.ProspectService;

@Service("prospectService")
public class ProspectServiceImpl implements ProspectService{
	
	@Autowired
	private ProspectRepository prospectRepository;

	@Override
	public Optional<Prospect> findById(Integer id) {
		return prospectRepository.findById(id);
	}

	@Override
	public Prospect save(Prospect entity) {
		return prospectRepository.save(entity);
	}

	@Override
	public void delete(Prospect entity) {
		prospectRepository.delete(entity);
	}

	@Override
	public Page<Prospect> findAllByPersonDniLikeAndPersonAssessor(String dni,Person assessor,Pageable pageable) {
		return prospectRepository.findAllByPersonDniLikeAndPersonAssessor(dni,assessor,pageable); 
	}
	
	@Override
	public Page<Prospect> findAllByPersonDniLikeAndPersonSupervisor(String dni,Person supervisor,Pageable pageable) {
		return prospectRepository.findAllByPersonDniLikeAndPersonSupervisor(dni, supervisor,pageable); 
	}

	@Override
	public Page<Prospect> findAllByPersonDniLike(String dni, Pageable pageable) {
		return prospectRepository.findAllByPersonDniLike(dni, pageable);
	}

	@Override
	public Prospect findByPerson(Person entity) {
		return prospectRepository.findByPerson(entity);
	}

	@Override
	public List<Prospect> findAllByPersonAssessor(Person assessor) {
		return prospectRepository.findAllByPersonAssessor(assessor);
	}

	@Override
	public List<Prospect> findAllByPersonSupervisor(Person supervisor) {
		return prospectRepository.findAllByPersonSupervisor(supervisor);
	}

	@Override
	public List<Prospect> findAll() {
		return prospectRepository.findAll();
	}

}
