package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.repository.ComisionesRepository;
import com.model.aldasa.service.ComisionesService;

@Service("comisionesService")
public class ComisionesServiceImpl implements ComisionesService  {

	
	@Autowired
	private ComisionesRepository comisionesRepository;

	@Override
	public Optional<Comisiones> findById(Integer id) {
		// TODO Auto-generated method stub
		return comisionesRepository.findById(id);
	}

	@Override
	public Comisiones save(Comisiones entity) {
		// TODO Auto-generated method stub
		return comisionesRepository.save(entity);
	}

	@Override
	public void delete(Comisiones entity) {
		// TODO Auto-generated method stub
		comisionesRepository.delete(entity);
	}

	@Override
	public Comisiones findByLote(Lote lote) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByLote(lote);
	}

	@Override
	public Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(
			boolean estado, String Status, Person personSupervisor, String dniAsesor, Date fechaIni, Date fechaFin,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(estado, Status, personSupervisor, dniAsesor, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<Comisiones> findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(
			boolean estado, String Status, String dniAsesor, Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(estado, Status, dniAsesor, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado,
			String Status, String tipoEmnpleado, Date fechaIni, Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(estado, Status, tipoEmnpleado, fechaIni, fechaFin, pageable);
	}

	@Override
	public List<Comisiones> findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(boolean estado,
			String Status, String tipoEmnpleado, Date fechaIni, Date fechaFin) {
		// TODO Auto-generated method stub
		return comisionesRepository.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(estado, Status, tipoEmnpleado, fechaIni, fechaFin);
	}

	
	

}
