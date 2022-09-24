package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.PersonRepository;
import com.model.aldasa.service.PersonService;

@Service("personService")
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Override
	public Optional<Person> findById(Integer id) {
		return personRepository.findById(id);
	}

	@Override
	public Person save(Person entity) {
		return personRepository.save(entity);
	}

	@Override
	public void delete(Person entity) {
		personRepository.delete(entity);
	}

	@Override
	public List<Person> findByStatus(Boolean status) {
		return personRepository.findByStatus(status);
	}
	
	@Override
	public Page<Person> findAllByStatus(Boolean status, Pageable pageable) {
		/*Aqui estoy intentando hacer dinamica la query (aun no me sale)
		 * ExampleMatcher matcher = ExampleMatcher.matching()
			    .withMatcher("names", match -> match.contains())
			    .withIgnorePaths("population");
		
		Person person = new Person();
		person.setNames("Ro");
	    Example<Person> example = Example.of(person, matcher);*/
		return personRepository.findAllByStatus(status, pageable);
	}
	
	@Override
	public Person findByDni(String dni) {
		return personRepository.findByDni(dni);
	}
	
	@Override
	public Person findByDniException(String dni, int idUser) {
		return personRepository.findByDniException(dni, idUser);
	}

}
