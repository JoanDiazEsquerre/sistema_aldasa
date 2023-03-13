package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Voucher;

public interface VoucherService {

	Optional<Voucher> findById(Integer id);
	Voucher save(Voucher entity);
	void delete(Voucher entity);
	
	Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(CuentaBancaria cuentaBancaria, 
			Double monto, String tipoTransaccion, String numeroTransaccion, Date fechaOperacion);
	
	List<Voucher> findByEstado(String estado);
}
