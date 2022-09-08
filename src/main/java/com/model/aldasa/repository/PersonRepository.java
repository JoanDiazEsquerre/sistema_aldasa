package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>{

	List<Person> findByStatus(String status);
}
