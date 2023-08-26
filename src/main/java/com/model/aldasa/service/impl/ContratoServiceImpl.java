package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.service.ContratoService;

@Service("contratoService")
public class ContratoServiceImpl implements ContratoService{
	
	@Autowired
	private ContratoRepository contratoRepository;

	@Override
	public Optional<Contrato> findById(Integer id) {
		return contratoRepository.findById(id);
	}

	@Override
	public Contrato save(Contrato entity) {
		return contratoRepository.save(entity);
	}

	@Override
	public void delete(Contrato entity) {
		contratoRepository.delete(entity); 
	}

	@Override
	public List<Contrato> findAll() {
		// TODO Auto-generated method stub
		return (List<Contrato>) contratoRepository.findAll();
	}

	@Override
	public Page<Contrato> findByEstado(boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstado(status, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal,String project, String manzana, String numeroLote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteProjectNameLikeAndLoteManzanaNameLikeAndLoteNumberLoteLike(status, sucursal, project, manzana, numeroLote, pageable);
	}

	@Override
	public Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(
			String personVenta, String dni, boolean estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(personVenta, dni, estado, cancelacionTotal, sucursal, pageable);
	}



	


}
