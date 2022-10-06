package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Team;

public interface TeamService {
	
	Optional<Team> findById(Integer id);
	Team save(Team team) ;
	void delete(Team team);
	List<Team> findByStatus(boolean status);
	Team findByName (String name);
	Team findByNameException(String name, int idTeam);

}
