package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comision;

public interface ComisionRepository  extends JpaRepository<Comision, Integer>{
	
	Page<Comision> findByEstadoAndFechaIniYear(Boolean estado,int anio, Pageable pageable);


}
