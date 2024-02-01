package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;

public interface ComisionesRepository extends JpaRepository<Comisiones, Integer> {
	
	Comisiones findByLote(Lote lote); 

	Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(boolean estado, String Status, Person personSupervisor,String dniAsesor, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(boolean estado, String Status,String dniAsesor, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado, String Status,String tipoEmnpleado, Date fechaIni, Date fechaFin, Pageable pageable);

	Page<Comisiones> findByEstadoAndConfiguracionComision(boolean estado, ConfiguracionComision comision, Pageable pageable);
	Page<Comisiones> findByEstadoAndConfiguracionComisionAndPersonSupervisor(boolean estado, ConfiguracionComision comision, Person personSupervisor, Pageable pageable);

	
	List<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado, String Status,String tipoEmnpleado, Date fechaIni, Date fechaFin);
	List<Comisiones> findByEstadoAndConfiguracionComision(boolean estado, ConfiguracionComision comision);
	List<Comisiones> findByEstadoAndConfiguracionComisionAndPersonAsesor(boolean estado, ConfiguracionComision comision, Person personAsesor);

}
