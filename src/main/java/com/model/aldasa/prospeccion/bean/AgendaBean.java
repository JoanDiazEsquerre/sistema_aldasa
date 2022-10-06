package com.model.aldasa.prospeccion.bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

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
	
	private ScheduleModel eventModel= new DefaultScheduleModel();
  
    private ProspectionDetail prospectionSelection;
    
    private ScheduleModel lazyEventModel;
 
	
	@PostConstruct
	public void init() {
		
//		
//		DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
//	                .title(detalle.getAction().getDescription())
//	                .startDate(date)
//	                .endDate(date)
//	                .description("Aqui la descripcion")
//	                .build();
//		eventModel.addEvent(event);
//	}
//}
		
		
//		DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder().title("Champions League Match")
//				.startDate(previousDay8Pm(date)).endDate(previousDay11Pm(date)).description("Team A vs. Team B")
//				.url("https://www.uefa.com/uefachampionsleague/").borderColor("orange").build();
//		eventModel.addEvent(event);
//
//		DefaultScheduleEvent<?> event1 = DefaultScheduleEvent.builder().startDate(date.minusDays(6)).endDate(date.minusDays(3))
//				.overlapAllowed(true).editable(false).resizable(false).backgroundColor("lightgreen").build();
//		eventModel.addEvent(event1);
//
//		
//
//		DefaultScheduleEvent<?> event2event = DefaultScheduleEvent.builder().title("Breakfast at Tiffanys (always resizable)")
//				.startDate(nextDay9Am(date)).endDate(nextDay11Am(date)).description("all you can eat")
//				.overlapAllowed(true).resizable(true).borderColor("#27AE60").build();
//		eventModel.addEvent(event2event);
//
//		event = DefaultScheduleEvent.builder().title("Plant the new garden stuff (always draggable)")
//				.startDate(theDayAfter3Pm(date)).endDate(fourDaysLater3pm(date)).description("Trees, flowers, ...")
//				.draggable(true).borderColor("#27AE60").build();
//		eventModel.addEvent(event);
//
//		DefaultScheduleEvent<?> scheduleEventAllDay = DefaultScheduleEvent.builder().title("Holidays (AllDay)")
//				.startDate(sevenDaysLater0am(date)).endDate(eightDaysLater0am(date))
//				.description("sleep as long as you want").borderColor("#27AE60").allDay(true).build();
//		eventModel.addEvent(scheduleEventAllDay);
		
   	}
	
	   public LocalDateTime getRandomDateTime(LocalDateTime base) {
	        LocalDateTime dateTime = base.withMinute(0).withSecond(0).withNano(0);
	        return dateTime.plusDays(((int) (Math.random() * 30)));
	    }
	   
	public void onPageLoad(){
		usuarioLogin = usuarioService.findByUsername(navegacionBean.getUsername());		
		
		
		eventModel= new DefaultScheduleModel();
		
		lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(LocalDateTime start, LocalDateTime end) {
            	if (usuarioLogin.getProfile().getName().equals(Perfiles.Administrador.name())) {
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionStatusAndScheduled("En seguimiento",true);
        		}else if(usuarioLogin.getProfile().getName().equals(Perfiles.Asesor.name())) {	
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonAssessorAndProspectionStatusAndScheduled(usuarioLogin.getPerson(),"En seguimiento",true);
        		} else if (usuarioLogin.getProfile().getName().equals(Perfiles.Supervisor.name())) {
        			lstProspectionDetailAgenda = prospectionDetailService.findByProspectionPersonSupervisorAndProspectionStatusAndScheduled(usuarioLogin.getPerson(),"En seguimiento",true);
        		}
        		
            
            	
        		if(!ObjectUtils.isEmpty(lstProspectionDetailAgenda)) {
	    			for(ProspectionDetail detalle : lstProspectionDetailAgenda) {
	    				Date input = detalle.getDate();
	    				Instant instant = input.toInstant();
	    				ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
	    				LocalDateTime date = zdt.toLocalDateTime();
	    					    			
	    				DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
	    						
	    						.title(detalle.getAction().getDescription())
	    						.startDate(date)
	    						.data(detalle)
	    						.endDate(date)
	    						.description(detalle.getProspection().getProspect().getPerson().getSurnames()+" "+detalle.getProspection().getProspect().getPerson().getNames()+""+(detalle.getComment().equals("")?"":" / "+detalle.getComment()))
	    						.overlapAllowed(true)
	    						.id(detalle.getId()+"")
	    						.borderColor("#CB4335").build();
	    				addEvent(event);
	    			}
	    		}
            	
            }
        };
		
//		if(!lstProspectionDetailAgenda.isEmpty() || lstProspectionDetailAgenda !=null) {
//			for(ProspectionDetail detalle : lstProspectionDetailAgenda) {
//				Date input = detalle.getDate();
//				Instant instant = input.toInstant();
//				ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
//				LocalDateTime date = zdt.toLocalDateTime();
//					
//				DefaultScheduleEvent<?> event2 = DefaultScheduleEvent.builder()
//						.title(detalle.getAction().getDescription())
//						.startDate(date)
//						.data(detalle)
//						.endDate(date)
//						.description(detalle.getProspection().getProspect().getPerson().getSurnames()+" "+detalle.getProspection().getProspect().getPerson().getNames()+""+(detalle.getComment().equals("")?"":" / "+detalle.getComment()))
//						.overlapAllowed(true)
//						.id(detalle.getId()+"")
//						.borderColor("#CB4335").build();
//				eventModel.addEvent(event2);
//			}
//		}
			
	}
	
    

    
   public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
	   ScheduleEvent event = selectEvent.getObject();
	   prospectionSelection = (ProspectionDetail) event.getData();
	   
   }
   
	enum Perfiles{
	    Administrador, 
	    Asesor, 
	    Supervisor;
	}
     
 
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public LocalDate getInitialDate() {
        return LocalDate.now().plusDays(1);
    }

     

	public ProspectionDetail getProspectionSelection() {
		return prospectionSelection;
	}

	public void setProspectionSelection(ProspectionDetail prospectionSelection) {
		this.prospectionSelection = prospectionSelection;
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

	public ScheduleModel getLazyEventModel() {
		return lazyEventModel;
	}

	public void setLazyEventModel(ScheduleModel lazyEventModel) {
		this.lazyEventModel = lazyEventModel;
	}

}
