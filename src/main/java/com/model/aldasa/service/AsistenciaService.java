package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Usuario;

public interface AsistenciaService {

	Optional<Asistencia> findById(Integer id);
	Asistencia save(Asistencia entity);
	void delete(Asistencia entity);
	
	Asistencia findByEmpleadoPerson (Person empleado);
	
	List<Asistencia> findByEmpleadoAndHoraBetweenOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin);
	List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin);

	
	Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(String dni, String tipo, Date fechaIni, Date fechaFin, Pageable pageable);

}
