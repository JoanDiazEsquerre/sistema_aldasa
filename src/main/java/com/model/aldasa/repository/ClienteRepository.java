package com.model.aldasa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.model.aldasa.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
