package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;

public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>{

	List<Person> findByStatus(Boolean status);
	Page<Person> findAllByDniLikeAndSurnamesLikeAndStatus(String dni, String names, Boolean status, Pageable pageable);
	Person findByDni(String dni);
	
	@Query(nativeQuery = true,value = " SELECT * FROM person  WHERE dni =:dni and id<>:idUser")
    Person findByDniException(String dni, int idUser);
}
