package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Imagen;

public interface ImagenRepository extends PagingAndSortingRepository<Imagen, Integer>{
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);
	List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria, String tipoTransaccion);

	Imagen findByNombre(String nombre);



}
