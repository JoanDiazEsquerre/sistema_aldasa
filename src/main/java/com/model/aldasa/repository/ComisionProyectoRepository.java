package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.Project;

public interface ComisionProyectoRepository  extends JpaRepository<ComisionProyecto, Integer>{
	
	ComisionProyecto findByComisionAndProyectoAndEstado(Comision comision, Project proyecto, boolean estado);
	List<ComisionProyecto> findByComisionAndEstado(Comision comision, boolean estado);


}
