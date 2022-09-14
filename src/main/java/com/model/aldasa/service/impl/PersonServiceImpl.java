package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.PersonRepository;
import com.model.aldasa.service.PersonService;

@Service("personService")
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private PersonRepository clientRepository;
	
	@Override
	public Optional<Person> findById(Integer id) {
		return clientRepository.findById(id);
	}

	@Override
	public Person save(Person entity) {
		return clientRepository.save(entity);
	}

	@Override
	public void delete(Person entity) {
		clientRepository.delete(entity);
	}

	@Override
	public List<Person> findByStatus(Boolean status) {
		return clientRepository.findByStatus(status);
	}

}
