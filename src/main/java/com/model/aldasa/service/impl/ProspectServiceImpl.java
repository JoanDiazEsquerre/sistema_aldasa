package com.model.aldasa.service.impl;

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
		// TODO Auto-generated method stub
		return prospectRepository.findById(id);
	}

	@Override
	public Prospect save(Prospect entity) {
		// TODO Auto-generated method stub
		return prospectRepository.save(entity);
	}

	@Override
	public void delete(Prospect entity) {
		// TODO Auto-generated method stub
		prospectRepository.delete(entity);
	}

	@Override
	public Page<Prospect> findAllByPersonDniLikeAndUsuarioAssessor(String dni,Usuario usuarioAssessor,Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectRepository.findAllByPersonDniLikeAndUsuarioAssessor(dni,usuarioAssessor,pageable); 
	}
	
	@Override
	public Page<Prospect> findAllByPersonDniLikeAndUsuarioAssessorTeamPersonSupervisor(String dni,Person supervisor,Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectRepository.findAllByPersonDniLikeAndUsuarioAssessorTeamPersonSupervisor(dni, supervisor,pageable); 
	}

	@Override
	public Page<Prospect> findAllByPersonDniLike(String dni, Pageable pageable) {
		// TODO Auto-generated method stub
		return prospectRepository.findAllByPersonDniLike(dni, pageable);
	}

	@Override
	public Prospect findByPerson(Person entity) {
		// TODO Auto-generated method stub
		return prospectRepository.findByPerson(entity);
	}

}
