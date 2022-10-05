package com.model.aldasa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.repository.ProspectionDetailRepository;
import com.model.aldasa.service.ProspectionDetailService;

@Service("prospectionDetailService")
public class ProspectionDetailServiceImpl implements ProspectionDetailService{
	
	@Autowired
	private ProspectionDetailRepository prospectionDetailRepository;

	@Override
	public Optional<ProspectionDetail> findById(Integer id) {
		return prospectionDetailRepository.findById(id);
	}

	@Override
	public ProspectionDetail save(ProspectionDetail entity) {
		return prospectionDetailRepository.save(entity);
	}

	@Override
	public void delete(ProspectionDetail prospectionDetail) {
		prospectionDetailRepository.delete(prospectionDetail);
		
	}

	@Override
	public List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled) {
		return prospectionDetailRepository.findByProspectionAndScheduled(prospection,scheduled); 
	}

}
