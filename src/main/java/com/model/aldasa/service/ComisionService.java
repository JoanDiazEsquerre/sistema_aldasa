package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Country;
import com.model.aldasa.entity.Department;
import com.model.aldasa.entity.Manzana;
import com.model.aldasa.entity.Usuario;

public interface ComisionService {
	
	Optional<Comision> findById(Integer id);
	Comision save(Comision entity);
	void delete(Comision entity);
	
	List<Comision> findAll();
	Page<Comision> findByEstadoAndFechaIniYear(Boolean estado,int anio, Pageable pageable);

}
