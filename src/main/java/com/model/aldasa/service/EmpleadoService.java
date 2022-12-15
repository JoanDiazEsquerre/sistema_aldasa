package com.model.aldasa.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;

public interface EmpleadoService {
	
	Optional<Empleado> findBy(Integer id);
	Empleado save(Empleado empleado);
	void delete(Empleado empleado);
	
	Empleado findByPerson (Person person);
	Empleado findByPersonIdException(int idPerson, int idEmpleado);

	
	Page<Empleado> findByPersonSurnamesLikeAndEstado(String person, boolean status, Pageable pageable);

}
