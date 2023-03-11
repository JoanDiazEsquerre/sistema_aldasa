package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Cliente;

public interface ClienteService {
	
	Optional<Cliente> findById(Integer id);
	Cliente save(Cliente entity);
	void delete(Cliente entity);

}
