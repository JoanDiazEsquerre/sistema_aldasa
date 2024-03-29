package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;

public interface CuotaService {

	Optional< Cuota> findById(Integer id);
	Cuota save( Cuota entity);
	void delete( Cuota entity);
	
	List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado);
	List<Cuota> findByContratoAndEstado(Contrato contrato, boolean estado);
	List<Cuota> findByContratoAndOriginal(Contrato contrato, boolean original);
	List<Cuota> findByPagoTotalAndEstadoAndContratoOrderById(String pagoTotal, boolean estado, Contrato contrato);
	List<Cuota> findByContrato(Contrato contrato);

	Page<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVenta(String pagoTotal, boolean estado,Person contratoPersonVenta, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectNameAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(String pagoTotal, boolean estado, String personSurnames, String personDni, String proyecto, Sucursal sucursal, String numLote, String manzana,Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLikeAndContratoLoteProjectSucursalAndContratoLoteNumberLoteLikeAndContratoLoteManzanaNameLike(String pagoTotal, boolean estado, String personSurnames, String personDni, Sucursal sucursal,String numLote, String manzana, Pageable pageable);

	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoBetween(String pagoTotal, boolean estado, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoLessThan(String pagoTotal, boolean estado, Date fechaIni, Pageable pageable);

	Page<Cuota> findByPagoTotalAndEstadoAndPrepago(String pagoTotal, boolean estado, boolean prepago, Pageable pageable);




}
