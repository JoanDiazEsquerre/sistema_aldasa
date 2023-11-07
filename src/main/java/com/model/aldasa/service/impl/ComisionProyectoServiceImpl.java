package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.Project;
import com.model.aldasa.repository.ComisionProyectoRepository;
import com.model.aldasa.repository.ComisionRepository;
import com.model.aldasa.service.ComisionProyectoService;
import com.model.aldasa.service.ComisionService;

@Service("comisionProyectoService")
public class ComisionProyectoServiceImpl  implements ComisionProyectoService {
	
	@Autowired
	private ComisionProyectoRepository comisionProyectoRepository;

	@Override
	public Optional<ComisionProyecto> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionProyectoRepository.findById(id);
	}

	@Override
	public ComisionProyecto save(ComisionProyecto entity) {
		// TODO Auto-generated method stub
		return comisionProyectoRepository.save(entity);
	}

	@Override
	public void delete(ComisionProyecto entity) {
		// TODO Auto-generated method stub
		comisionProyectoRepository.delete(entity);
	}

	@Override
	public ComisionProyecto findByComisionAndProyectoAndEstado(Comision comision, Project proyecto,
			boolean estado) {
		// TODO Auto-generated method stub
		return comisionProyectoRepository.findByComisionAndProyectoAndEstado(comision, proyecto, estado);
	}

	@Override
	public List<ComisionProyecto> findByComisionAndEstado(Comision comision, boolean estado) {
		// TODO Auto-generated method stub
		return comisionProyectoRepository.findByComisionAndEstado(comision, estado);
	}

	

	

	

}
