package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;

public interface ProspectRepository extends PagingAndSortingRepository<Prospect, Integer> {
	
	Page<Prospect> findByPersonSurnamesLikeAndPersonDniLikeAndPersonAssessor(String surnamesPerson, String dni,Person assessor,Pageable pageable);
	Page<Prospect> findByPersonAssessorSurnamesLikeAndPersonSurnamesLikeAndPersonDniLikeAndPersonSupervisor(String surnamesAssessor, String surnamesPerson, String dni,Person supervisor,Pageable pageable);
	Page<Prospect> findByPersonSupervisorSurnamesLikeAndPersonAssessorSurnamesLikeAndPersonSurnamesLikeAndPersonDniLike(String surnamesSupervisor, String surnamesAssessor, String surnamesPerson, String dni,Pageable pageable);
	Prospect findByPerson(Person entity);
	
	List<Prospect> findByPersonAssessor(Person assessor);
	List<Prospect> findByPersonSupervisor(Person supervisor);
	List<Prospect> findAll();

}
