package com.model.aldasa.service;

import java.util.List;
import java.util.Optional;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;

public interface ProspectionDetailService {
	
	Optional<ProspectionDetail> findById(Integer id);
	ProspectionDetail save(ProspectionDetail entity);
	void delete(ProspectionDetail entity);
	List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled);
}
