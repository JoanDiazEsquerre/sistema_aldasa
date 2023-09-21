package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.CuotaRepository;
import com.model.aldasa.service.CuotaService;

@Service("cuotaService")
public class CuotaServiceImpl implements CuotaService{

	@Autowired
	private  CuotaRepository cuotaRepository;

	@Override
	public Optional<Cuota> findById(Integer id) {
		// TODO Auto-generated method stub
		return cuotaRepository.findById(id);
	}

	@Override
	public Cuota save(Cuota entity) {
		// TODO Auto-generated method stub
		return cuotaRepository.save(entity);
	}

	@Override
	public void delete(Cuota entity) {
		// TODO Auto-generated method stub
		cuotaRepository.delete(entity);
	}

	@Override
	public List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstado(pagoTotal, estado);
	}

	@Override
	public List<Cuota> findByContratoAndEstado(Contrato contrato, boolean estado) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContratoAndEstado(contrato, estado);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstado(pagoTotal, estado, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(
			String pagoTotal, boolean estado, String personSurnames, String personDni, String proyecto, Sucursal sucursal,String numLote, String manzana, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(pagoTotal, estado, personSurnames, personDni, proyecto, sucursal,numLote,manzana,pageable);
	}
	
	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(
			String pagoTotal, boolean estado, String personSurnames, String personDni, Sucursal sucursal, String numLote, String manzana,Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(pagoTotal, estado, personSurnames, personDni, sucursal,numLote,manzana, pageable);
	}

	@Override
	public List<Cuota> findByContratoAndOriginal(Contrato contrato, boolean original) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContratoAndOriginal(contrato, original);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVenta(String pagoTotal, boolean estado,
			Person contratoPersonVenta, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoPersonVenta(pagoTotal, estado, contratoPersonVenta, pageable);
	}

	@Override
	public List<Cuota> findByPagoTotalAndEstadoAndContratoOrderById(String pagoTotal, boolean estado,
			Contrato contrato) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndContratoOrderById(pagoTotal, estado, contrato);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoBetween(String pagoTotal, boolean estado, Date fechaIni,
			Date fechaFin, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndFechaPagoBetween(pagoTotal, estado, fechaIni, fechaFin, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoLessThan(String pagoTotal, boolean estado, Date fechaIni,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndFechaPagoLessThan(pagoTotal, estado, fechaIni, pageable);
	}

	@Override
	public Page<Cuota> findByPagoTotalAndEstadoAndPrepago(String pagoTotal, boolean estado, boolean prepago, Pageable pageable) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByPagoTotalAndEstadoAndPrepago(pagoTotal, estado, prepago, pageable);
	}

	@Override
	public List<Cuota> findByContrato(Contrato contrato) {
		// TODO Auto-generated method stub
		return cuotaRepository.findByContrato(contrato);
	}

	


	


}
