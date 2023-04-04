package com.model.aldasa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Person;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	Cliente findByPersonAndEstado (Person person, boolean estado);
	Cliente findByPersonAndEstadoAndPersonaNatural (Person person, boolean estado, boolean personaNatural);



}
