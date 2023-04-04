package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Voucher;

public interface DetalleDocumentoVentaRepository extends JpaRepository<DetalleDocumentoVenta, Integer>{
	
	DetalleDocumentoVenta findByVoucherIdAndEstado(int voucher, boolean estado);

	List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);

}
