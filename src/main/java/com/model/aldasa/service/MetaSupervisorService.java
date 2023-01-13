package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Area;
import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;

public interface MetaSupervisorService {
	
	Optional<MetaSupervisor> findById(Integer id);
	MetaSupervisor save(MetaSupervisor entity);
	void delete(MetaSupervisor entity);
	
	List<MetaSupervisor> findByComisionAndEstado(Comision comision ,boolean estado);
	MetaSupervisor findByComisionAndEstadoAndPersonSupervisor(Comision comision ,boolean estado, Person supervisor);

}
