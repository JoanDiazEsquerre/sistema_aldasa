package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.CuentaBancaria;

public interface CuentaBancariaService {
	
	Optional<CuentaBancaria> findById(Integer id);
	CuentaBancaria save(CuentaBancaria entity);
	void delete(CuentaBancaria entity);
	List<CuentaBancaria> findByEstadoAndMonedaLike(boolean estado, String moneda);


}
