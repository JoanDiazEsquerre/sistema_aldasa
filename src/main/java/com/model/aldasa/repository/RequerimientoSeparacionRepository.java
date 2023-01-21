package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;

public interface RequerimientoSeparacionRepository extends JpaRepository<RequerimientoSeparacion, Integer> {
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);


}
