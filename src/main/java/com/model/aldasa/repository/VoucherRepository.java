package com.model.aldasa.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Voucher;


public interface VoucherRepository extends JpaRepository<Voucher, Integer>{
	
	Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(CuentaBancaria cuentaBancaria, 
			Double monto, String tipoTransaccion, String numeroTransaccion, Date fechaOperacion);

}
