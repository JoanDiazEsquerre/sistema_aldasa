package com.model.aldasa.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Inventario;

public interface ImagenService {
	
	Optional<Imagen> findById(Integer id);
	Imagen save(Imagen entity);
	void delete(Imagen entity);
	
	List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado);
	List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria, String tipoTransaccion);
	
	Imagen findByNombre(String nombre);


}
