package com.model.aldasa.service;

import java.util.Optional;

import com.model.aldasa.entity.Area;

public interface AreaService {
	
	Optional<Area> findById(Integer id);
	Area save(Area entity);
	void delete(Area entity);

}
