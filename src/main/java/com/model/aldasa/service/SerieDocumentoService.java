package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.SerieDocumento;
import com.model.aldasa.entity.Sucursal;

public interface SerieDocumentoService {

	Optional<SerieDocumento> findById(Integer id);
	SerieDocumento save(SerieDocumento entity);
	void delete(SerieDocumento entity);
	
	List<SerieDocumento> findByTipoDocumentoAndSucursal(String tipoDocumento, Sucursal sucural);

}
