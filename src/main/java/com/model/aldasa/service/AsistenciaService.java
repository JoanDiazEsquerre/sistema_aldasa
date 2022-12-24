package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;

public interface AsistenciaService {

	Optional<Asistencia> findById(Integer id);
	Asistencia save(Asistencia entity);
	void delete(Asistencia entity);
	
	List<Asistencia> findByEmpleadoAndHoraBetweenOrderByIdAsc(Empleado empleado, Date horaIni, Date horaFin);

}
