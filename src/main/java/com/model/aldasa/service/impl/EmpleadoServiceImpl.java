package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
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
	public List<Empleado> findByEstado(boolean estado) {
		// TODO Auto-generated method stub
		return empleadoRepository.findByEstado(estado);
	}

}
