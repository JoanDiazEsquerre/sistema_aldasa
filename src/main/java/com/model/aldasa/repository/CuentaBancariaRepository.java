package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Profile;


public interface CuentaBancariaRepository  extends JpaRepository<CuentaBancaria, Integer>  {

	List<CuentaBancaria> findByEstado(boolean estado);

}
