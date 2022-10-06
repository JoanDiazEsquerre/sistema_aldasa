package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Integer>{

	List<Team> findByStatus(Boolean status);
	Team findByName(String name);
	
	@Query(nativeQuery = true,value = "SELECT * FROM team WHERE name=:namee AND id <> :idTeam ")
    Team findByNameException(String namee, int idTeam);
	
}
