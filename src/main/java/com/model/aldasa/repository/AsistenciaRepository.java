package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;

public interface AsistenciaRepository  extends JpaRepository<Asistencia, Integer> {
	
	List<Asistencia> findByEmpleadoAndHoraBetweenOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin);
	List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin);
	
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraDesc(String dni, String tipo, Date fechaIni, Date fechaFin);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraAsc(String dni, String tipo, Date fechaIni, Date fechaFin);

	Asistencia findByEmpleadoPerson (Person empleado);

	Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndEmpleadoSucursalAndHoraBetween(String dni, String tipo,Sucursal sucursal, Date fechaIni, Date fechaFin, Pageable pageable);

	
}
