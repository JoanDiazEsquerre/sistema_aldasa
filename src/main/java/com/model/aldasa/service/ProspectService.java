package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;

public interface ProspectService {
	
	Optional<Prospect> findById(Integer id);
	Prospect save(Prospect entity);
	
	void delete(Prospect entity);
	Page<Prospect> findAllByPersonDniLikeAndPersonAssessor(String dni,Person assessor,Pageable pageable);
	Page<Prospect> findAllByPersonDniLikeAndPersonSupervisor(String dni,Person supervisor,Pageable pageable);
	Page<Prospect> findAllByPersonDniLike(String dni , Pageable pageable);

	Prospect findByPerson(Person entity);
	
	List<Prospect> findByPersonAssessor(Person assessor);
	List<Prospect> findByPersonSupervisor(Person supervisor);
	List<Prospect> findAll();

}
