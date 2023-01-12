package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;

public interface ComisionesService {

	Optional<Comisiones> findById(Integer id);
	Comisiones save(Comisiones entity);
	void delete(Comisiones entity);
	
	Comisiones findByLote(Lote lote); 
	
	Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(boolean estado, String Status, Person personSupervisor,String dniAsesor, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(boolean estado, String Status,String dniAsesor, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado, String Status,String tipoEmnpleado, Date fechaIni, Date fechaFin, Pageable pageable);
	
	List<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado, String Status,String tipoEmnpleado, Date fechaIni, Date fechaFin);

}
