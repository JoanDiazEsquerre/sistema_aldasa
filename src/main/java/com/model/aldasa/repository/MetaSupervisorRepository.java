package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;

public interface MetaSupervisorRepository  extends JpaRepository<MetaSupervisor, Integer> {
	
	List<MetaSupervisor> findByComisionAndEstado(Comision comision ,boolean estado);
	MetaSupervisor findByComisionAndEstadoAndPersonSupervisor(Comision comision ,boolean estado, Person supervisor);

}
