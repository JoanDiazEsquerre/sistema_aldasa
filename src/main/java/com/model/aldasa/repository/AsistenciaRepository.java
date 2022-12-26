package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;

public interface AsistenciaRepository  extends JpaRepository<Asistencia, Integer> {
	
	List<Asistencia> findByEmpleadoAndHoraBetweenOrderByIdAsc(Empleado empleado, Date horaIni, Date horaFin);

	Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin, Pageable pageable);

	
}
