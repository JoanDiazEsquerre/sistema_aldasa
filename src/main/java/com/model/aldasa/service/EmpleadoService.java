package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;
import com.model.aldasa.entity.Sucursal;

public interface EmpleadoService {
	
	Optional<Empleado> findBy(Integer id);
	Empleado save(Empleado empleado);
	void delete(Empleado empleado);
	
	Empleado findByPerson (Person person);
	Empleado findByPersonIdException(int idPerson, int idEmpleado);
	Empleado findByPersonDni(String dniPerson);
	List<Empleado> findByEstadoOrderByPersonSurnamesAsc(boolean estado);

	Page<Empleado> findByPersonSurnamesLikeAndEstado(String person, boolean status, Pageable pageable);
	
	
	Page<Empleado> findByEstadoAndSucursal(boolean status, Sucursal sucursal, Pageable pageable);
	Page<Empleado> findByPersonAndEstadoAndSucursal(Person person, boolean status, Sucursal sucursal, Pageable pageable);
	Page<Empleado> findByEstadoAndAreaAndSucursal(boolean estado, Area area, Sucursal sucursal, Pageable pageable);
	Page<Empleado> findByPersonAndEstadoAndAreaAndSucursal(Person person, boolean status, Area area, Sucursal sucursal, Pageable pageable);
	
	List<Empleado> findByEstado(boolean status);
	List<Empleado> findByPersonAndEstado(Person person, boolean status);
	List<Empleado> findByEstadoAndArea(boolean estado, Area area);
	List<Empleado> findByPersonAndEstadoAndArea(Person person, boolean status, Area area);
	

	

}
