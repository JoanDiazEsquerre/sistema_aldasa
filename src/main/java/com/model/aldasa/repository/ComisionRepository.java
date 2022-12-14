package com.model.aldasa.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comision;

public interface ComisionRepository  extends JpaRepository<Comision, Integer>{
	
	Page<Comision> findByEstadoAndFechaIniBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);


}
