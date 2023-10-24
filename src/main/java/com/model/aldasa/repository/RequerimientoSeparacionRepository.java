package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.Sucursal;

public interface RequerimientoSeparacionRepository extends JpaRepository<RequerimientoSeparacion, Integer> {
	
	RequerimientoSeparacion findAllByLoteAndEstado(Lote lote, String estado);
	
	List<RequerimientoSeparacion> findByProspection(Prospection prostection);
	
	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursal(String estado, Sucursal sucursal, Pageable pageable);

	Page<RequerimientoSeparacion> findAllByEstadoAndLoteProjectSucursalAndGeneraDocumento(String estado, Sucursal sucursal, boolean generaDocuento,Pageable pageable);



}
