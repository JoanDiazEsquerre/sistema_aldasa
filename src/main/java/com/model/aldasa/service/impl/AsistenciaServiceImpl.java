package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.AsistenciaRepository;
import com.model.aldasa.service.AsistenciaService;

@Service("asistenciaService")
public class AsistenciaServiceImpl  implements AsistenciaService  {


	@Autowired
	private AsistenciaRepository asistenciaRepository;
	
	@Override
	public Optional<Asistencia> findById(Integer id) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findById(id);
	}

	@Override
	public Asistencia save(Asistencia entity) {
		// TODO Auto-generated method stub
		return asistenciaRepository.save(entity);
	}

	@Override
	public void delete(Asistencia entity) {
		// TODO Auto-generated method stub
		asistenciaRepository.delete(entity);
	}

	@Override
	public List<Asistencia> findByEmpleadoAndHoraBetweenOrderByHoraAsc(Empleado empleado, Date horaIni, Date horaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoAndHoraBetweenOrderByHoraAsc(empleado, horaIni, horaFin);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(String dni, String tipo, Date fechaIni,
			Date fechaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetween(dni, tipo, fechaIni, fechaFin);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniAndTipoAndHoraBetween(String dni, String tipo, Date fechaIni,
			Date fechaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniAndTipoAndHoraBetween(dni, tipo, fechaIni, fechaFin);
	}

	@Override
	public Asistencia findByEmpleadoPerson(Person empleado) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPerson(empleado);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraDesc(String dni, String tipo,
			Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraDesc(dni, tipo, fechaIni, fechaFin);
	}

	@Override
	public List<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraAsc(String dni, String tipo,
			Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndHoraBetweenOrderByHoraAsc(dni, tipo, fechaIni, fechaFin);
	}

	@Override
	public Page<Asistencia> findByEmpleadoPersonDniLikeAndTipoLikeAndEmpleadoSucursalAndHoraBetween(String dni,
			String tipo, Sucursal sucursal, Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoPersonDniLikeAndTipoLikeAndEmpleadoSucursalAndHoraBetween(dni, tipo, sucursal, fechaIni, fechaFin, pageable);
	}

}
