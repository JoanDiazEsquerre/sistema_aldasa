package com.model.aldasa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Sucursal;

public interface PlantillaVentaRepository extends JpaRepository<PlantillaVenta, Integer>{

	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursal(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Pageable pageable);
	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonSupervisor(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Person supervisor, Pageable pageable);
	Page<PlantillaVenta> findByEstadoAndProspectoPersonSurnamesLikeAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLikeAndLoteProjectSucursalAndProspectoPersonAssessor(String estado, String prospect, String project, String manzana, String lote, Sucursal sucursal, Person asesor, Pageable pageable);

}
