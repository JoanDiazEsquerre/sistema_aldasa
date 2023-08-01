package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity. DetalleDocumentoVenta;
import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Voucher;

public interface DetalleDocumentoVentaService {

	Optional<DetalleDocumentoVenta> findById(Integer id);
	DetalleDocumentoVenta save(DetalleDocumentoVenta entity);
	void delete(DetalleDocumentoVenta entity);
	
	DetalleDocumentoVenta findByVoucherIdAndEstado(int voucher, boolean estado);
	
	List<DetalleDocumentoVenta> findByDocumentoVentaAndEstado(DocumentoVenta documentoVenta, boolean estado);

}
