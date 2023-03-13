package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Cuota;

public interface CuotaRepository  extends JpaRepository<Cuota, Integer> {
	
	List<Cuota> findByPagoTotalAndEstado(String pagoTotal, boolean estado);

}
