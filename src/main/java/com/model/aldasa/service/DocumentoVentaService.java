package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.model.aldasa.entity.DocumentoVenta;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Empresa;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.TipoDocumento;

public interface DocumentoVentaService {

	Optional<DocumentoVenta> findById(Integer id);
	DocumentoVenta save(DocumentoVenta entity);
	void delete(DocumentoVenta entity);
		
	List<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin);
	List<DocumentoVenta> findByDocumentoVentaRefAndTipoDocumentoAndEstado(DocumentoVenta documentoVenta, TipoDocumento tipoDocumento, boolean estado);
	
	Page<DocumentoVenta> findByEstadoAndSucursalEmpresaAndFechaEmisionBetween(boolean estado, Empresa empresa, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndFechaEmisionBetween(boolean estado, Sucursal sucursal, Date fechaIni, Date fechaFin, Pageable pageable);
	Page<DocumentoVenta> findByEstadoAndSucursalAndRazonSocialLikeAndNumeroLikeAndRucLike(boolean estado, Sucursal sucursal, String razonSocial, String numero, String ruc, Pageable pageable);

	


}
