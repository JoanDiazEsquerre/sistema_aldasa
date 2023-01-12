package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Empleado;

public interface ComisionRepository  extends JpaRepository<Comision, Integer>{
	
	List<Comision> findByEstado(Boolean estado);
	Comision findByEstadoAndCodigo(Boolean estado, String codigo);
	Comision findByFechaIniLessThanEqualAndFechaCierreGreaterThanEqual(Date fechaIni, Date fechaCierre);
	
	@Query(nativeQuery = true,value = "SELECT * FROM comision WHERE codigo=:codigo AND id<>:idComision ")
	Comision findByCodigoAndIdException(String codigo, int idComision);

	Page<Comision> findByEstadoAndFechaIniBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);


}
