package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;


public interface ContratoService {

	Optional<Contrato> findById(Integer id);
	Contrato saveGeneraComision(Contrato entity);
	Contrato save(Contrato entity);
	void delete(Contrato entity);
	List<Contrato> findAll();
	List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(boolean status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal);
	
	Page<Contrato> findByEstado(boolean status, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal, Project project, String manzana, String numLote, Pageable pageable);
	Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(String personVenta, String dni, boolean estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable);

	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal, String manzana, String numLote, Pageable pageable);




}
