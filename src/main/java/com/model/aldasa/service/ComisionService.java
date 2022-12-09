package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.Manzana;

public interface ComisionService {
	
	Optional<Comision> findById(Integer id);
	Comision save(Comision entity);
	void delete(Comision entity);
	
	List<Comision> findAll();

}
