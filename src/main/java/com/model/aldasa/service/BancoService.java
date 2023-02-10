package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Banco;

public interface BancoService {

	Optional<Banco> findById(Integer id);
	Banco save(Banco entity);
	void delete(Banco entity);
}
