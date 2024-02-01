package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ConfiguracionComision;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.MetaSupervisorRepository;
import com.model.aldasa.service.MetaSupervisorService;

@Service("metaSupervisorService")
public class MetaSupervisorServiceImpl  implements MetaSupervisorService {
	
	@Autowired
	private MetaSupervisorRepository metaSupervisorRepository;

	@Override
	public Optional<MetaSupervisor> findById(Integer id) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findById(id);
	}

	@Override
	public MetaSupervisor save(MetaSupervisor entity) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.save(entity);
	}

	@Override
	public void delete(MetaSupervisor entity) {
		// TODO Auto-generated method stub
		metaSupervisorRepository.delete(entity);
	}

	@Override
	public List<MetaSupervisor> findByConfiguracionComisionAndEstado(ConfiguracionComision comision, boolean estado) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findByConfiguracionComisionAndEstado(comision, estado);
	}

	@Override
	public MetaSupervisor findByConfiguracionComisionAndEstadoAndPersonSupervisor(ConfiguracionComision comision, boolean estado,
			Person supervisor) {
		// TODO Auto-generated method stub
		return metaSupervisorRepository.findByConfiguracionComisionAndEstadoAndPersonSupervisor(comision, estado, supervisor);
	}

}
