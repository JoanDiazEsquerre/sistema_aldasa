package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Person;

public interface CuotaRepository  extends JpaRepository<Cuota, Integer> {
	
	List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado);
	List<Cuota> findByContratoAndEstado(Contrato contrato, boolean estado);
	
	Page<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVenta(String pagoTotal, boolean estado,Person contratoPersonVenta, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndContratoPersonVentaSurnamesLikeAndContratoPersonVentaDniLike(String pagoTotal, boolean estado, String personSurnames, String personDni, Pageable pageable);
	List<Cuota> findByContratoAndOriginal(Contrato contrato, boolean original);
	List<Cuota> findByPagoTotalAndEstadoAndContratoOrderById(String pagoTotal, boolean estado, Contrato contrato);
	
	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoBetween(String pagoTotal, boolean estado, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<Cuota> findByPagoTotalAndEstadoAndFechaPagoLessThan(String pagoTotal, boolean estado, Date fechaIni, Pageable pageable);





}
