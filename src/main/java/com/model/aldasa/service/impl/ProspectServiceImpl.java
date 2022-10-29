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
	public Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessor(String dniPerson,String surnamesPerson,Person assessor,Pageable pageable) {
		return prospectRepository.findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessor( dniPerson, surnamesPerson, assessor, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonAssessor(String surnamesPerson,Person assessor,Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonAssessor( surnamesPerson, assessor, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisor(String dni, String surnamesPerson,Person supervisor,Pageable pageable) {
		return prospectRepository.findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisor(dni, surnamesPerson, supervisor, pageable); 
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonSupervisor(String surnamesPerson,Person supervisor,Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonSupervisor(surnamesPerson, supervisor, pageable); 
	}

	@Override
	public Page<Prospect> findByPersonSurnamesLikeAndPersonDniLike(String surnamesPerson, String dni , Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLikeAndPersonDniLike(surnamesPerson, dni, pageable);
	}
	
	@Override
	public Page<Prospect> findByPersonSurnamesLike(String surnamesPerson, Pageable pageable) {
		return prospectRepository.findByPersonSurnamesLike(surnamesPerson, pageable);
	}

	@Override
	public Prospect findByPerson(Person entity) {
		return prospectRepository.findByPerson(entity);
	}

	@Override
	public List<Prospect> findByPersonAssessor(Person assessor) {
		return prospectRepository.findByPersonAssessor(assessor);
	}

	@Override
	public List<Prospect> findByPersonSupervisor(Person supervisor) {
		return prospectRepository.findByPersonSupervisor(supervisor);
	}

	@Override
	public List<Prospect> findAll() {
		return prospectRepository.findAll();
	}

}
