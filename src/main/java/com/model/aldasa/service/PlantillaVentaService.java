package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.Sucursal;

public interface PlantillaVentaService {

	Optional<PlantillaVenta> findById(Integer id);
	PlantillaVenta save(PlantillaVenta entity);
	void delete(PlantillaVenta entity);
	
	//para administrador
	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Pageable pageable);

	//para supervisor
	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonSupervisor(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Person supervisor, Pageable pageable);

	
	//para asesor
	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonAssessor(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Person asesor, Pageable pageable);

}
