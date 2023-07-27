package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.TipoDocumento;

public interface TipoDocumentoRepository extends PagingAndSortingRepository<TipoDocumento, Integer>{

	List<TipoDocumento> findByEstado(boolean estado);
	List<TipoDocumento> findByEstadoAndCodigoLike(boolean estado, String codigo);

}
