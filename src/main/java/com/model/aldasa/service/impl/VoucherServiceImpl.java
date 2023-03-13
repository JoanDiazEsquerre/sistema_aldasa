package com.model.aldasa.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Voucher;
import com.model.aldasa.repository.CuentaBancariaRepository;
import com.model.aldasa.repository.VoucherRepository;
import com.model.aldasa.service.VoucherService;


@Service("voucherService")
public class VoucherServiceImpl implements VoucherService {

	@Autowired
	private VoucherRepository voucherRepository;

	@Override
	public Optional<Voucher> findById(Integer id) {
		// TODO Auto-generated method stub
		return voucherRepository.findById(id);
	}

	@Override
	public Voucher save(Voucher entity) {
		// TODO Auto-generated method stub
		return voucherRepository.save(entity);
	}

	@Override
	public void delete(Voucher entity) {
		// TODO Auto-generated method stub
		voucherRepository.delete(entity);
	}

	@Override
	public Voucher findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(
			CuentaBancaria cuentaBancaria, Double monto, String tipoTransaccion, String numeroTransaccion,
			Date fechaOperacion) {
		// TODO Auto-generated method stub
		return voucherRepository.findByCuentaBancariaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaOperacion(cuentaBancaria, monto, tipoTransaccion, numeroTransaccion, fechaOperacion);
	}

	@Override
	public List<Voucher> findByEstado(String estado) {
		// TODO Auto-generated method stub
		return voucherRepository.findByEstado(estado);
	}


	
}
