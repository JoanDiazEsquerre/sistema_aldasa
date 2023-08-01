package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Identificador;
import com.model.aldasa.entity.TipoOperacion;

public interface IdentificadorRepository extends JpaRepository<Identificador, Integer>{

	List<Identificador> findByEstado(boolean estado);
}
