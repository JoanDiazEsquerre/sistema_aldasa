package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;

public interface ClienteService {
	
	Optional<Cliente> findById(Integer id);
	Cliente save(Cliente entity);
	void delete(Cliente entity);
	
	Cliente findByPersonAndEstado (Person person, boolean estado);
	Cliente findByPersonAndEstadoAndPersonaNatural (Person person, boolean estado, boolean personaNatural);
	List<Cliente> findByEstado (boolean estado);



}
