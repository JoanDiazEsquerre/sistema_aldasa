package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Cliente;
import com.model.aldasa.entity.Person;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	
	Cliente findByPersonAndEstado (Person person, boolean estado);
	Cliente findByPersonAndEstadoAndPersonaNatural (Person person, boolean estado, boolean personaNatural);
	List<Cliente> findByEstado (boolean estado);
	
	Page<Cliente> findByRazonSocialLikeAndNombreComercialLikeAndRucLikeAndDniLikeAndEstado(String razonSocial,String noombreComercial,String ruc, String dni, boolean status, Pageable pageable);




}
