package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Usuario;

public interface AsistenciaService {

	Optional<Asistencia> findById(Integer id);
	Asistencia save(Asistencia entity);
	void delete(Asistencia entity);
	
	Asistencia findByEmpleadoPerson (Person empleado);
	
	List<Asistencia> findByEmpleadoAndHoraBetweenAndEstadoOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetweenAndEstado(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraDesc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);
	List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenAndEstadoOrderByHoraAsc(String dni, String tipo, Date fechaIni, Date fechaFin, boolean estado);

	
	Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndEmpleadoSucursalAndHoraBetweenAndEstado(String dni, String tipo,Sucursal sucursal, Date fechaIni, Date fechaFin, boolean estado, Pageable pageable);

}
