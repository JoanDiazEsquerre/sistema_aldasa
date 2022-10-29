package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;

public interface ProspectRepository extends PagingAndSortingRepository<Prospect, Integer> {
	
	//para asesor
	Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonAssessor(String dniPerson,String surnamesPerson,Person assessor,Pageable pageable);
	Page<Prospect> findByPersonSurnamesLikeAndPersonAssessor(String surnamesPerson,Person assessor,Pageable pageable);
	
	
	//Para supervisor
		Page<Prospect> findByPersonDniLikeAndPersonSurnamesLikeAndPersonSupervisor(String dni, String surnamesPerson,Person supervisor,Pageable pageable);
		Page<Prospect> findByPersonSurnamesLikeAndPersonSupervisor(String surnamesPerson,Person supervisor,Pageable pageable);
		
		
	//para administrador
	Page<Prospect> findByPersonSurnamesLikeAndPersonDniLike(String surnamesPerson, String dni , Pageable pageable);
	Page<Prospect> findByPersonSurnamesLike(String surnamesPerson, Pageable pageable);
	
	
	Prospect findByPerson(Person entity);
	
	List<Prospect> findByPersonAssessor(Person assessor);
	List<Prospect> findByPersonSupervisor(Person supervisor);
	List<Prospect> findAll();

}
