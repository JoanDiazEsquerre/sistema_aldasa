package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.repository.ImagenRepository;
import com.model.aldasa.service.ImagenService;

@Service("imagenService")
public class ImagenServiceImpl implements ImagenService{
	
	@Autowired
	private ImagenRepository imagenRepository;

	@Override
	public Optional<Imagen> findById(Integer id) {
		// TODO Auto-generated method stub
		return imagenRepository.findById(id);
	}

	@Override
	public Imagen save(Imagen entity) {
		// TODO Auto-generated method stub
		return imagenRepository.save(entity);
	}

	@Override
	public void delete(Imagen entity) {
		// TODO Auto-generated method stub
		imagenRepository.delete(entity);
	}

	@Override
	public List<Imagen> findByNombreLikeAndEstado(String nombre, boolean estado) {
		// TODO Auto-generated method stub
		return imagenRepository.findByNombreLikeAndEstado(nombre, estado);
	}

	@Override
	public Imagen findByNombre(String nombre) {
		// TODO Auto-generated method stub
		return imagenRepository.findByNombre(nombre);
	}

	@Override
	public List<Imagen> findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion( 
			boolean estado, Date fecha, BigDecimal monto, String numOperacion, CuentaBancaria cuentaBancaria,
			String tipoTransaccion) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(estado, fecha, monto, numOperacion, cuentaBancaria, tipoTransaccion);
	}

	@Override
	public List<Imagen> findByDocumentoVenta(DocumentoVenta documentoVenta) {
		// TODO Auto-generated method stub
		return imagenRepository.findByDocumentoVenta(documentoVenta);
	} 

	@Override
	public Page<Imagen> findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeOrderByIdDesc(boolean estado, String sucursal,String numeroOperacion, String numCuenta, String tipoTrasaccion,Pageable pageable) {
		// TODO Auto-generated method stub
		return imagenRepository.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeOrderByIdDesc(estado, sucursal, numeroOperacion, numCuenta, tipoTrasaccion, pageable);
	} 

}
