package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospect;
import com.model.aldasa.entity.Usuario;

public interface ProspectRepository extends PagingAndSortingRepository<Prospect, Integer> {
	
	Page<Prospect> findAllByPersonDniLikeAndUsuarioAssessor(String dni,Usuario usuarioAssessor,Pageable pageable);
	Page<Prospect> findAllByPersonDniLikeAndUsuarioAssessorTeamPersonSupervisor(String dni,Person supervisor,Pageable pageable);
	Page<Prospect> findAllByPersonDniLike(String dni,Pageable pageable);
	Prospect findByPerson(Person entity);

}
