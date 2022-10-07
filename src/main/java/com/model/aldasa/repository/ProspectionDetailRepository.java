package com.model.aldasa.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Prospection;
import com.model.aldasa.entity.ProspectionDetail;

public interface ProspectionDetailRepository extends PagingAndSortingRepository<ProspectionDetail, Integer> {
	
	List<ProspectionDetail> findByProspectionAndScheduled(Prospection prospection,boolean scheduled);
	
	List<ProspectionDetail> findByProspectionStatusAndScheduledAndDateBetween(String prospectionStatus,boolean scheduled,Date dateStart,Date dateFinish);
	List<ProspectionDetail> findByProspectionPersonAssessorAndProspectionStatusAndScheduledAndDateBetween(Person personAssessor,String prospectionStatus,boolean scheduled,Date dateStart,Date dateFinish);
	List<ProspectionDetail> findByProspectionPersonSupervisorAndProspectionStatusAndScheduledAndDateBetween(Person personSupervisor,String prospectionStatus,boolean scheduled,Date dateStart,Date dateFinish);

}
