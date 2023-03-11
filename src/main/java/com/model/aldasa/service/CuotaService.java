package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Cuota;

public interface CuotaService {

	Optional< Cuota> findById(Integer id);
	Cuota save( Cuota entity);
	void delete( Cuota entity);
}
