package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comision;

public interface ComisionRepository  extends JpaRepository<Comision, Integer>{
	
	List<Comision> findByEstado(Boolean estado);
	Comision findByEstadoAndCodigo(Boolean estado, String codigo);

	Page<Comision> findByEstadoAndFechaIniBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);


}
