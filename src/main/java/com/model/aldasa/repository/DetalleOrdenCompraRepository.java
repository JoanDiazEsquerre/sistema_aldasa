package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.DetalleOrdenCompra;
import com.model.aldasa.entity.OrdenCompra;

public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Integer> {
	
	List<DetalleOrdenCompra> findByOrdenCompraAndEstado(OrdenCompra ordenCompra, boolean estado);


}
