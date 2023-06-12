package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.EmpleadoRepository;
import com.model.aldasa.service.EmpleadoService;

@Service("empleadoService")
public class EmpleadoServiceImpl implements EmpleadoService {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Override
	public Optional<Empleado> findBy(Integer id) {
		return empleadoRepository.findById(id);
	}

	
	@Override
	public Empleado save(Empleado entity) {
		return empleadoRepository.save(entity);
	}
	
	@Override
	public void delete(Empleado entity) {
		empleadoRepository.delete(entity);
	}


	@Override
	public Page<Empleado> findByPersonSurnamesLikeAndEstado(String person, boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonSurnamesLikeAndEstado(person, status, pageable);
	}


	@Override
	public Empleado findByPerson(Person person) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPerson(person);
	}


	@Override
	public Empleado findByPersonIdException(int idPerson, int idEmpleado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonIdException(idPerson, idEmpleado);
	}


	@Override
	public Empleado findByPersonDni(String dniPerson) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonDni(dniPerson);
	}


	@Override
	public List<Empleado> findByEstadoOrderByPersonSurnamesAsc(boolean estado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoOrderByPersonSurnamesAsc(estado);
	}

	@Override
	public List<Empleado> findByEstado(boolean status) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstado(status);
	}


	@Override
	public List<Empleado> findByPersonAndEstado(Person person, boolean status) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstado(person, status);
	}


	@Override
	public List<Empleado> findByEstadoAndArea(boolean estado, Area area) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndArea(estado, area);
	}


	@Override
	public List<Empleado> findByPersonAndEstadoAndArea(Person person, boolean status, Area area) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndArea(person, status, area);
	}


	@Override
	public Page<Empleado> findByEstadoAndSucursal(boolean status, Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndSucursal(status, sucursal, pageable);
	}


	@Override
	public Page<Empleado> findByPersonAndEstadoAndSucursal(Person person, boolean status, Sucursal sucursal,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndSucursal(person, status, sucursal, pageable);
	}


	@Override
	public Page<Empleado> findByEstadoAndAreaAndSucursal(boolean estado, Area area, Sucursal sucursal,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstadoAndAreaAndSucursal(estado, area, sucursal, pageable);
	}


	@Override
	public Page<Empleado> findByPersonAndEstadoAndAreaAndSucursal(Person person, boolean status, Area area,
			Sucursal sucursal, Pageable pageable) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByPersonAndEstadoAndAreaAndSucursal(person, status, area, sucursal, pageable);
	}




}
