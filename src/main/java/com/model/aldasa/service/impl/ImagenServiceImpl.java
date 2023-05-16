package com.model.aldasa.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Imagen;
import com.model.aldasa.repository.ImagenRepository;
import com.model.aldasa.service.ImagenService;

@Service("imagenService")
public class ImagenServiceImpl implements ImagenService{
	
	@Autowired
	private ImagenRepository imagenRepository;

	@Override
	public Optional<Imagen> findById(Integer id) {
		// TODO Auto-generated method stub
		return imagenRepository.findById(id);
	}

	@Override
	public Imagen save(Imagen entity) {
		// TODO Auto-generated method stub
		return imagenRepository.save(entity);
	}

	@Override
	public void delete(Imagen entity) {
		// TODO Auto-generated method stub
		imagenRepository.delete(entity);
	}

}
