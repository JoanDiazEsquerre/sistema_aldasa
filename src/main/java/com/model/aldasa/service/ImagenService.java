package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;

public interface ImagenService {
	
	Optional<Imagen> findById(Integer id);
	Imagen save(Imagen entity);
	void delete(Imagen entity);
	
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);
	List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria, String tipoTransaccion);
	List<Imagen> findByDocumentoVenta(DocumentoVenta documentoVenta);
	List<Imagen> findByEstadoAndCuentaBancariaAndFechaBetweenOrderByFechaDesc(boolean estado, CuentaBancaria cuentaBancaria, Date fechaIni, Date fechaFin);
	List<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndFechaBetweenOrderByFechaDesc(boolean estado, String sucursal, Date fechaIni, Date fechaFin);

	
	
	Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeAndFechaBetweenOrderByIdDesc(boolean estado, String sucursal, String numeroOperacion, String numCuenta, String tipoTrasaccion, Date fechaIni, Date fechaFin,Pageable pageable);
	Page<Imagen> findByEstadoAndCuentaBancariaAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeAndFechaBetweenOrderByIdDesc(boolean estado, CuentaBancaria cuentaBancaria, String numeroOperacion, String numCuenta, String tipoTrasaccion, Date fechaIni, Date fechaFin,Pageable pageable);

	
	Imagen findByNombre(String nombre);


}
