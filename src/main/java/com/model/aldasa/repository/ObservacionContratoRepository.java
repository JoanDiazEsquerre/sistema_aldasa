package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.ObservacionContrato;

public interface ObservacionContratoRepository extends JpaRepository<ObservacionContrato, Integer> {

	List<ObservacionContrato> findByEstado(boolean estado);
}
