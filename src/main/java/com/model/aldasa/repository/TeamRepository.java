package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Integer>{

	List<Team> findByStatus(Boolean status);
	
}
