package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;

public interface PersonRepository extends PagingAndSortingRepository<Person, Integer>{

	List<Person> findByStatus(Boolean status);
	
}
