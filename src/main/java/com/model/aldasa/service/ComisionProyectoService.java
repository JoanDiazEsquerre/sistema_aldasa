package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;
import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.Project;

public interface ComisionProyectoService {
	
	Optional<ComisionProyecto> findById(Integer id);
	ComisionProyecto save(ComisionProyecto entity);
	void delete(ComisionProyecto entity);
	
	ComisionProyecto findByComisionAndProyectoAndEstado(Comision comision, Project proyecto, boolean estado);
	List<ComisionProyecto> findByComisionAndEstado(Comision comision, boolean estado);

	


}
