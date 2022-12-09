package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.repository.TeamRepository;
import com.model.aldasa.service.TeamService;

@Service("teamService")
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Optional<Team> findById(Integer id) {
		// TODO Auto-generated method stub
		return teamRepository.findById(id);
	}

	@Override
	public Team save(Team team) {
		// TODO Auto-generated method stub
		return teamRepository.save(team);
	}

	@Override
	public void delete(Team team) {
		// TODO Auto-generated method stub
		teamRepository.delete(team);
	}

	@Override
	public List<Team> findByStatus(boolean status) {
		// TODO Auto-generated method stub
		return teamRepository.findByStatus(status);
	}
	
	@Override
	public Team findByName(String name) {
		// TODO Auto-generated method stub
		return teamRepository.findByName(name);
	}

	@Override
	public Team findByNameException(String name, int idTeam) {
		// TODO Auto-generated method stub
		return teamRepository.findByNameException(name, idTeam);
	}
	
	@Override
	public Team findByPersonSupervisor(Person personSupervisor) {
		// TODO Auto-generated method stub
		return teamRepository.findByPersonSupervisor(personSupervisor);
	}
	
}
