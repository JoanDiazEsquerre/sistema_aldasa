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
	
	//Para asesor
	Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessor(String dniPerson,String surnamesPerson,Person assessor,Pageable pageable);
	Page<Prospect> findByPersonSurnamesLikeAndPersonAssessor(String surnamesPerson,Person assessor,Pageable pageable);
	
	
	//Para supervisor
	Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisor(String dni, String surnamesPerson,Person supervisor,Pageable pageable);
	Page<Prospect> findByPersonSurnamesLikeAndPersonSupervisor(String surnamesPerson,Person supervisor,Pageable pageable);

	
	//Para administrador
	Page<Prospect> findByPersonSurnamesLikeAndPersonDniLike(String surnamesPerson, String dni , Pageable pageable);
	Page<Prospect> findByPersonSurnamesLike(String surnamesPerson, Pageable pageable);

	Prospect findByPerson(Person entity);
	
	List<Prospect> findByPersonAssessor(Person assessor);
	List<Prospect> findByPersonSupervisor(Person supervisor);
	List<Prospect> findAll();

}
