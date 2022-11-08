package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Lote;

public interface LoteService {
 
	Optional<Lote> findById(Integer id);
	Lote save(Lote lote);
	void delete(Lote lote);
	List<Lote> findByStatus(boolean status);
	Lote findByNumberLote (String name);
	Lote findByNumberLoteException(String name, int idLote);
}
