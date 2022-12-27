package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Profile;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
	
	Page<Empleado> findByPersonSurnamesLikeAndEstado(String person, boolean status, Pageable pageable);
	Empleado findByPerson (Person person);
	Empleado findByPersonDni(String dniPerson);
	List<Empleado> findByEstadoOrderByPersonSurnamesAsc(boolean estado);


	@Query(nativeQuery = true,value = "SELECT * FROM empleado WHERE idPerson=:idPerson AND id<>:idEmpleado ")
	Empleado findByPersonIdException(int idPerson, int idEmpleado);

}
