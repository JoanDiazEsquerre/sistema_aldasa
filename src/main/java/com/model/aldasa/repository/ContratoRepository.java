package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;

public interface ContratoRepository extends PagingAndSortingRepository<Contrato, Integer> {
	
	Contrato findByLoteAndEstado(Lote lote, boolean estado);
	
	List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(boolean status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal);

	
	Page<Contrato> findByEstado(boolean status, Pageable pageable);
	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal, Project project, String manzana, String numLote, Pageable pageable);
	Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(String personVenta, String dni, boolean estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable);

	Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal, String manzana, String numLote, Pageable pageable);

}
