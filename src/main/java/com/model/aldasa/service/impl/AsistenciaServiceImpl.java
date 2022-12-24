package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Asistencia;
import com.model.aldasa.entity.Empleado;
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
	public List<Asistencia> findByEmpleadoAndHoraBetweenOrderByIdAsc(Empleado empleado, Date horaIni, Date horaFin) {
		// TODO Auto-generated method stub
		return asistenciaRepository.findByEmpleadoAndHoraBetweenOrderByIdAsc(empleado, horaIni, horaFin);
	}

}
