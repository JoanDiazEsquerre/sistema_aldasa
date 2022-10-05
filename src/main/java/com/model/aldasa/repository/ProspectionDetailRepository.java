package com.model.aldasa.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;

public interface ProspectionDetailRepository extends PagingAndSortingRepository<ProspectionDetail, Integer> {
	
	List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled);

}
