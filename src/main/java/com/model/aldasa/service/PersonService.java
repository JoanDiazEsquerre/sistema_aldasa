package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Person;

public interface PersonService {
	
	Optional<Person> findById(Integer id);
	Person save(Person client);
	void delete(Person client);
	List<Person> findByStatus(String status);

}
