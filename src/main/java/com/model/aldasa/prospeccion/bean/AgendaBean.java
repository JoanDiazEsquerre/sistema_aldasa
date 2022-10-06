package com.model.aldasa.prospeccion.bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.model.aldasa.entity.ProspectionDetail;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.ProspectionDetailService;
import com.model.aldasa.service.UsuarioService;

@Component
@ManagedBean
@SessionScoped
public class AgendaBean {
	
	@Inject
	private NavegacionBean navegacionBean;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProspectionDetailService prospectionDetailService;
	
	private Usuario usuarioLogin = new Usuario();
	private List<ProspectionDetail> lstProspectionDetailAgenda = new ArrayList<>();
	
	private ScheduleModel eventModel;
 
    private ScheduleModel lazyEventModel;
 
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
 
	
	@PostConstruct
	public void init() {
		
		
   	}
	
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());		
		
		if (usuarioLogin.getProfile().getName().equals(Perfiles.Administrador.toString())) {
			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionStatusAndScheduled("En seguimiento",true);
		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.Asesor.toString())) {	
			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonAssessorAndProspectionStatusAndScheduled(usuarioLogin.getPerson(),"En seguimiento",true);
		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.Supervisor.toString())) {
			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonSupervisorAndProspectionStatusAndScheduled(usuarioLogin.getPerson(),"En seguimiento",true);
		}
		
		if(!lstProspectionDetailAgenda.isEmpty() || lstProspectionDetailAgenda != null) {
			eventModel = new DefaultScheduleModel();
			for(ProspectionDetail detalle : lstProspectionDetailAgenda) {
				
				Date input = detalle.getDate();
				Instant instant = input.toInstant();
				ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
				LocalDateTime date = zdt.toLocalDateTime();
				
				DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
			                .title(detalle.getAction().getDescription())
			                .startDate(date)
			                .endDate(date)
			                .description("Aqui la descripcion")
			                .build();
				eventModel.addEvent(event);
			}
		}
	}
	
	enum Perfiles{
	    Administrador, 
	    Asesor, 
	    Supervisor;
	}
     
 
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }
     
    public LocalDate getInitialDate() {
        return LocalDate.now().plusDays(1);
    }
 
    public ScheduleEvent<?> getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent<?> event) {
        this.event = event;
    }
     
     
    public void onEventSelect(SelectEvent<ScheduleEvent> selectEvent) {
        event = selectEvent.getObject();
    }
     
	
	
	
	

	

	public ProspectionDetailService getProspectionDetailService() {
		return prospectionDetailService;
	}

	public void setProspectionDetailService(ProspectionDetailService prospectionDetailService) {
		this.prospectionDetailService = prospectionDetailService;
	}

	public List<ProspectionDetail> getLstProspectionDetailAgenda() {
		return lstProspectionDetailAgenda;
	}

	public void setLstProspectionDetailAgenda(List<ProspectionDetail> lstProspectionDetailAgenda) {
		this.lstProspectionDetailAgenda = lstProspectionDetailAgenda;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public void setLazyEventModel(ScheduleModel lazyEventModel) {
		this.lazyEventModel = lazyEventModel;
	}

	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}

	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public Usuario getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(Usuario usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}

}
