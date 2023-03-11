package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.SerieDocumento;

public interface SerieDocumentoService {

	Optional<SerieDocumento> findById(Integer id);
	SerieDocumento save(SerieDocumento entity);
	void delete(SerieDocumento entity);
}
