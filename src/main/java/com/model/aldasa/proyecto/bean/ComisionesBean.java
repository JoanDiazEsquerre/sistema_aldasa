package com.model.aldasa.proyecto.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Comisiones;
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Meta;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.ComisionesService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.MetaSupervisorService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.service.UsuarioService;
import com.model.aldasa.util.EstadoLote;
import com.model.aldasa.util.Perfiles;

@ManagedBean
@ViewScoped
public class ComisionesBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{teamService}")
	private TeamService teamService; 
	
	@ManagedProperty(value = "#{usuarioService}")
	private UsuarioService usuarioService; 
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{comisionService}")
	private ComisionService comisionService;
	
	@ManagedProperty(value = "#{comisionesService}")
	private ComisionesService comisionesService;
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	@ManagedProperty(value = "#{personService}")
	private PersonService personService;
	
	@ManagedProperty(value = "#{metaSupervisorService}")
	private MetaSupervisorService metaSupervisorService;
	
	private LazyDataModel<Comisiones> lstComisionesLazy;
	private LazyDataModel<Usuario> lstUsuarioLazy;
//	private LazyDataModel<Lote> lstLoteVendidoLazy;
	
	private Team teamSelected;
	private Comision comisionSelected;
	private Comisiones comisionesSelected;
	private Lote loteSelected;
	
	private String opcionAsesor = "";

	private Date fechaIni,fechaFin;
	private Integer comisionContado=8;
	private Integer comisionCredito=4;
		
	private double totalSolesContado = 0;
	private double totalSolesInicial = 0;
	private double totalSolesPendiente = 0;
	
//	private List<Person> lstPersonAsesor;
	private List<Comision> lstComision;
	private List<Comisiones> lstComisiones;
	private List<Lote> lstLotesVendidos;
	private List<Meta> lstMeta;
	private List<Person> lstPersonSupervisor;
	
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	
	@PostConstruct
	public void init() {
		comisionSelected = comisionService.findByEstadoAndCodigo(true, sdfM.format(new Date())+sdfY2.format(new Date()));
		cambiarComision();
		lstComision = comisionService.findByEstado(true);
		
		iniciarLazyComisiones();
	}
	
	public void listarLotesVendidosPorTeam() {
		Date fechaIni = comisionSelected.getFechaIni();
		fechaIni.setHours(0);
		fechaIni.setMinutes(0);
		fechaIni.setSeconds(0);
		
		Date fechaFin = comisionSelected.getFechaCierre();
		fechaFin.setHours(23);
		fechaFin.setMinutes(59);
		fechaFin.setSeconds(59);
		
		if(teamSelected.getName().equals("ONLINE")) {
			
		}else if(teamSelected.getName().equals("EXTERNOS")){
			
		}else if(teamSelected.getName().equals("INTERNOS")) {
			
		}else {
			if(teamSelected.getPersonSupervisor()!=null) {
				lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), "%%", fechaIni, fechaFin);
				
			}
		}
		
	}
	
//	public void iniciarLazyLotesVendidos() {
//		lstLoteVendidoLazy = new LazyDataModel<Lote>() {
//			private List<Lote> datasource;
//
//            @Override
//            public void setRowIndex(int rowIndex) {
//                if (rowIndex == -1 || getPageSize() == 0) {
//                    super.setRowIndex(-1);
//                } else {
//                    super.setRowIndex(rowIndex % getPageSize());
//                }
//            }
//
//            @Override
//            public Lote getRowData(String rowKey) {
//                int intRowKey = Integer.parseInt(rowKey);
//                for (Lote lote : datasource) {
//                    if (lote.getId() == intRowKey) {
//                        return lote;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public String getRowKey(Lote lote) {
//                return String.valueOf(lote.getId());
//            }
//
//			@Override
//			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
//				
//				
//                                
//                Sort sort=Sort.by("numberLote").ascending();
//                if(sortBy!=null) {
//                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
//                	    System.out.println(entry.getKey() + "/" + entry.getValue());
//                	   if(entry.getValue().getOrder().isAscending()) {
//                		   sort = Sort.by(entry.getKey()).descending();
//                	   }else {
//                		   sort = Sort.by(entry.getKey()).ascending();
//                	   }
//                	}
//                }
//                
//                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
//                
//				Page<Lote> pageLote;
//				
//				Date fechaIni = comisionSelected.getFechaIni();
//				fechaIni.setHours(0);
//				fechaIni.setMinutes(0);
//				fechaIni.setSeconds(0);
//				
//				Date fechaFin = comisionSelected.getFechaCierre();
//				fechaFin.setHours(23);
//				fechaFin.setMinutes(59);
//				fechaFin.setSeconds(59);
//
//				pageLote= loteService.findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween("%"+EstadoLote.VENDIDO.getName()+"%","%"+teamSelected.getPersonSupervisor().getDni()+"%", "%%", fechaIni, fechaFin, pageable);
//				
//				setRowCount((int) pageLote.getTotalElements());
//				return datasource = pageLote.getContent();
//			}
//		};
//	}
	
//	public void verDetalleAsesores() {
//		lstPersonAsesor = new ArrayList<>();
//		List<Lote> lstLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), "%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
//		int contador = 0;
//		if(!lstLotes.isEmpty()) {
//			for(Lote lote : lstLotes) {
//				Person personAsesor = lote.getPersonAssessor();
//				
//				if(contador == 0) {
//					lstPersonAsesor.add(personAsesor);
//				}else {
//					boolean encuentra = false;
//					for(Person asesor:lstPersonAsesor) {
//						if(asesor.equals(lote.getPersonAssessor())) {
//							encuentra=true;
//						}
//					}
//					
//					if(!encuentra) {
//						lstPersonAsesor.add(personAsesor);
//					}
//				}
//				contador++;
//			}
//			
//			for(Person asesor:lstPersonAsesor) {
//				int nroContado = 0;
//				int nroCredito = 0;
//				double comisionContado = 0.0;
//				double comisionCredito = 0.0;
//				double totalComision = 0.0;
//				
//				for(Lote lote : lstLotes) {
//					if(asesor.equals(lote.getPersonAssessor())) {
//						int nro = asesor.getLotesVendidos()+1;
//						if(lote.getTipoPago().equals("Contado")) {
//							nroContado = nroContado+1;
//							totalComision = totalComision + calcularComisionAsesor(lote);
//							comisionContado = comisionContado+ calcularComisionAsesor(lote);
//						}else {
//							nroCredito = nroCredito+1;
//							totalComision = totalComision + calcularComisionAsesor(lote);
//							comisionCredito = comisionCredito+ calcularComisionAsesor(lote);
//						}
//						
//						asesor.setLotesVendidos(nro); 
//						asesor.setLotesContado(nroContado);
//						asesor.setLotesCredito(nroCredito);
//						asesor.setTotalComision(totalComision); 
//						asesor.setComisionContado(comisionContado);
//						asesor.setComisionCredito(comisionCredito); 
//					}
//				}
//			}
//		}
//	}
	
	public String asignarTipoBono(Person asesor) {
		String tipoBono = "Ninguno";
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			tipoBono = "Bono Junior";
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			tipoBono = "Bono Senior";
		}
		if(asesor.getLotesVendidos()>=9) {
			tipoBono = "Bono Master";
		}
		return tipoBono;
		
	}
	
	public double obtenerSueldoBasicoAsesor(Person asesor) {
		double sueldo = 0.0;
		Empleado empleado = empleadoService.findByPerson(asesor);
		if(empleado != null) {
			sueldo = empleado.getSueldoBasico();
		}
	
		return sueldo;
		
	}
	
	public double obtenerSueldoBasicoAsesorBono(Person asesor) {
		double sueldo = 0.0;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			sueldo = comisionSelected.getBasicoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			sueldo = comisionSelected.getBasicoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			sueldo = comisionSelected.getBasicoMaster();
		}
	
		return sueldo;
	}
	
	public double obtenerBonoAsesor(Person asesor) {
		double bono = 0.0;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			bono = comisionSelected.getBonoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			bono = comisionSelected.getBonoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			bono = comisionSelected.getBonoMaster();
		}
	
		return bono;
	}
	
	public double pagoMesAsesor(Person person) {
		double pagoMes = 0.0;
		String tipoBono = asignarTipoBono(person);
		if(tipoBono.equals("Ninguno")) {
			pagoMes = obtenerSueldoBasicoAsesor(person)+ person.getTotalComision();
		}else {
			pagoMes = person.getTotalComision()+ obtenerBonoAsesor(person)+obtenerSueldoBasicoAsesorBono(person);
		}
		
		return pagoMes;
	}
	
	public double calcularSueldoMesAsesor(Person asesor) {
		double sueldoMes = 0.0;
		if(asesor.getLotesVendidos()>=2 && asesor.getLotesVendidos()<=4) {
			sueldoMes = comisionSelected.getBonoJunior();
		}
		if(asesor.getLotesVendidos()>=5 && asesor.getLotesVendidos()<=8) {
			sueldoMes = comisionSelected.getBonoSenior();
		}
		if(asesor.getLotesVendidos()>=9) {
			sueldoMes = comisionSelected.getBonoMaster();
		}
	
		return sueldoMes;
	}
	
	public double obtenerTotoalComisionesAsesor(Person asesor) {
		double sueldo = 0.0;
		Empleado empleado = empleadoService.findByPerson(asesor);
		if(empleado != null) {
			sueldo = empleado.getSueldoBasico();
		}
	
		return sueldo;
		
	}
	
	public void cambiarComision() {
		
		lstPersonSupervisor = personService.getPersonSupervisor(EstadoLote.VENDIDO.getName(), comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		
	}
	
	public double calcularComisionSubgerente(Lote lote) {

		double comision = 0;
		double porcSubgerente =  Double.parseDouble(comisionSelected.getSubgerente()+"")  / 100;
		comision = lote.getMontoVenta() * porcSubgerente;

		return comision;
	}
	
	
	
	public List<Lote> listaLotesVendidoPorAsesor(Person asesor){
		List<Lote> listaLotes = new ArrayList<>();
		listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), asesor.getDni(), comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		
		return listaLotes;
		
	}
	
	public double calcularComisionAsesor(Lote lote) {
		 double comision = 0;
		if (lote.getTipoPago().equals("Contado")) {
			double porcentaje = Double.parseDouble(comisionSelected.getComisionContado()+"") /100;
			comision = porcentaje * lote.getMontoVenta();
		}
		if(lote.getTipoPago().equals("Crédito")) {
			double porcentaje = Double.parseDouble(comisionSelected.getComisionCredito()+"") /100;
			comision = porcentaje * lote.getMontoVenta();
		}
		return  comision;
	}
	
	public double calcularSolesContado(Team team) {
		double solesContado = 0;
		
		if(team.getName().equals("INTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado + lote.getMontoVenta();
					}
				}
			}
			totalSolesContado = totalSolesContado +solesContado;
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado + lote.getMontoVenta();
					}
				}
			}
			totalSolesContado = totalSolesContado +solesContado;
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Contado")) {
						solesContado = solesContado + lote.getMontoVenta();
					}
				}
			}
			totalSolesContado = totalSolesContado +solesContado;
			
		}
		
		
		return solesContado;
	}
	
	public double calcularSolesInicial(Team team) {
		double solesCredito = 0;
		
		if(team.getName().equals("INTERNOS")) {
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito + lote.getMontoInicial();
					}
				}
			}
			totalSolesInicial = totalSolesInicial + solesCredito;
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito + lote.getMontoInicial();
					}
				}
			}
			totalSolesInicial = totalSolesInicial + solesCredito;
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Crédito")) {
						solesCredito = solesCredito + lote.getMontoInicial();
					}
				}
			}
			totalSolesInicial = totalSolesInicial + solesCredito;
		}
		
		
		
		return solesCredito;
	}
	
	public double calcularSolesPendiente(Team team) {
		double solesPendiente = 0;
		
		if(team.getName().equals("INTERNOS")) {
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						double saldo = lote.getMontoVenta() - lote.getMontoInicial();
						solesPendiente = solesPendiente+saldo;
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente + solesPendiente;
			
		}else if(team.getName().equals("EXTERNOS")) {
			
			List<Lote> listLotesVendido = new ArrayList<>();
			List<Usuario> listUsuInternos = usuarioService.findByTeam(team);
			if(!listUsuInternos.isEmpty()) {
				for(Usuario usuInt : listUsuInternos) {
					List<Lote> listLotesVendidoInt = loteService.findByStatusAndPersonAssessorDniAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), usuInt.getPerson().getDni(),comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
					if(!listLotesVendidoInt.isEmpty()) {
						for(Lote lote : listLotesVendidoInt) {
							listLotesVendido.add(lote);
						}
					}
				}
			}
			
			if(!listLotesVendido.isEmpty()) {
				for(Lote lote : listLotesVendido){
					if(lote.getTipoPago().equals("Crédito")) {
						double saldo = lote.getMontoVenta() - lote.getMontoInicial();
						solesPendiente = solesPendiente+saldo;
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente + solesPendiente;
			
		}else {
			List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
			if(!listaLotes.isEmpty()) {
				for(Lote lote : listaLotes){
					if(lote.getTipoPago().equals("Crédito")) {
						double saldo = lote.getMontoVenta() - lote.getMontoInicial();
						solesPendiente = solesPendiente+saldo;
					}
				}
			}
			totalSolesPendiente = totalSolesPendiente + solesPendiente;
		}
		
		
		
		return solesPendiente;
	}
	
	public void mostrarMeta() {
		totalSolesContado=0;
		totalSolesInicial=0;
		totalSolesPendiente=0;
		lstMeta = new ArrayList<>();
		List<Person> lstSupervisoresCampo = personService.getPersonSupervisorCampo(comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if (!lstSupervisoresCampo.isEmpty()) {
			for(Person persona : lstSupervisoresCampo) {
				List<Lote> lstLotesVendidos = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), persona, "%%", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
				
				Meta meta = new Meta();
				meta.setSupervisor(persona.getSurnames() + " " + persona.getNames());
				meta.setLotesVendidos(lstLotesVendidos.size());
				
				MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comisionSelected, true, persona);
				double calculo = 0;
				if(metaSupervisor != null) {
					calculo = (lstLotesVendidos.size()*100)/metaSupervisor.getMeta();
				}
				
				meta.setPorcentajeMeta((int) calculo);
				
				double contado = 0 ;
				double inicial = 0 ;
				double saldo = 0 ;
				
				for(Lote lote : lstLotesVendidos) {
					if(lote.getTipoPago().equals("Contado")) {
						contado = contado + lote.getMontoVenta();
					}else {
						inicial = inicial + lote.getMontoInicial();
						double calculaSaldo = lote.getMontoVenta() - lote.getMontoInicial();
						saldo =saldo + calculaSaldo ;
					}
				}
				totalSolesContado = totalSolesContado + contado;
				totalSolesInicial = totalSolesInicial + inicial;
				totalSolesPendiente = totalSolesPendiente + saldo;

				meta.setMontoContado(contado);
				meta.setMontoInicial(inicial);
				meta.setSaldoPendiente(saldo);
				lstMeta.add(meta);
			}
		}
		
		List<Comisiones> lstInternos = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "I", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(!lstInternos.isEmpty()) {
		
			Meta meta = new Meta() ;
			meta.setSupervisor("Internos");
			meta.setLotesVendidos(lstInternos.size());
			
			meta.setPorcentajeMeta(0);
			
			double contado = 0 ;
			double inicial = 0 ;
			double saldo = 0 ;
			
			for(Comisiones comisiones : lstInternos) {
				if(comisiones.getLote().getTipoPago().equals("Contado")) {
					contado = contado + comisiones.getLote().getMontoVenta();
				}else {
					inicial = inicial + comisiones.getLote().getMontoInicial();
					double calculoSaldo = comisiones.getLote().getMontoVenta() - comisiones.getLote().getMontoInicial();
					saldo = saldo + calculoSaldo;
				}
			}
			totalSolesContado = totalSolesContado + contado;
			totalSolesInicial = totalSolesInicial + inicial;
			totalSolesPendiente = totalSolesPendiente + saldo ;

			
			meta.setMontoContado(contado);
			meta.setMontoInicial(inicial);
			meta.setSaldoPendiente(saldo);
			lstMeta.add(meta);
		}
		
		List<Comisiones> lstExternos = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "E", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(!lstExternos.isEmpty()) {
		
			Meta meta = new Meta() ;
			meta.setSupervisor("Externos");
			meta.setLotesVendidos(lstExternos.size());
			meta.setPorcentajeMeta(0);
			
			double contado = 0 ;
			double inicial = 0 ;
			double saldo = 0 ;
			
			for(Comisiones comisiones : lstExternos) {
				if(comisiones.getLote().getTipoPago().equals("Contado")) {
					contado = contado + comisiones.getLote().getMontoVenta();
				}else {
					inicial = inicial + comisiones.getLote().getMontoInicial();
					double calculoSaldo = comisiones.getLote().getMontoVenta() - comisiones.getLote().getMontoInicial();
					saldo = saldo + calculoSaldo;
				}
			}
			totalSolesContado = totalSolesContado + contado;
			totalSolesInicial = totalSolesInicial + inicial;
			totalSolesPendiente = totalSolesPendiente + saldo ;
			
			meta.setMontoContado(contado);
			meta.setMontoInicial(inicial);
			meta.setSaldoPendiente(saldo);
			lstMeta.add(meta);
		}
		
		List<Comisiones> lstOnline = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "O", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(!lstOnline.isEmpty()) {
		
			Meta meta = new Meta() ;
			meta.setSupervisor("Online/"+ lstOnline.get(0).getLote().getPersonSupervisor().getSurnames() + " " + lstOnline.get(0).getLote().getPersonSupervisor().getNames());
			meta.setLotesVendidos(lstOnline.size());
			
			MetaSupervisor metaSupervisor = metaSupervisorService.findByComisionAndEstadoAndPersonSupervisor(comisionSelected, true, lstOnline.get(0).getLote().getPersonSupervisor());
			double calculo = 0;
			if(metaSupervisor != null) {
				calculo = (lstOnline.size()*100)/metaSupervisor.getMeta();
			}
			
			meta.setPorcentajeMeta((int) calculo);
						
			double contado = 0 ;
			double inicial = 0 ;
			double saldo = 0 ;
			
			for(Comisiones comisiones : lstOnline) {
				if(comisiones.getLote().getTipoPago().equals("Contado")) {
					contado = contado + comisiones.getLote().getMontoVenta();
				}else {
					inicial = inicial + comisiones.getLote().getMontoInicial();
					double calculoSaldo = comisiones.getLote().getMontoVenta() - comisiones.getLote().getMontoInicial();
					saldo = saldo + calculoSaldo;
				}
			}
			totalSolesContado = totalSolesContado + contado;
			totalSolesInicial = totalSolesInicial + inicial;
			totalSolesPendiente = totalSolesPendiente + saldo ;

			
			meta.setMontoContado(contado);
			meta.setMontoInicial(inicial);
			meta.setSaldoPendiente(saldo);
			lstMeta.add(meta);
		}
		
		
	}
	
	public void iniciarLazyUsuarioAsesor() {
		lstUsuarioLazy = new LazyDataModel<Usuario>() {
			private List<Usuario> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Usuario getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Usuario usuario : datasource) {
                    if (usuario.getId() == intRowKey) {
                        return usuario;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Usuario usuario) {
                return String.valueOf(usuario.getId());
            }

			@Override
			public List<Usuario> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
//				String numberLote="%"+ (filterBy.get("numberLote")!=null?filterBy.get("numberLote").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				String status = "%Vendido%";
				String dniAsesor = "%%";
				String dniSupervisor = "%%";
				
				if(teamSelected!=null)dniSupervisor ="%"+ teamSelected.getPersonSupervisor().getDni()+"%";
            
                Sort sort=Sort.by("personAssessor.surnames").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
				Page<Usuario> pageUsuario;
				pageUsuario= usuarioService.findByProfileNameLikeAndPersonSurnamesLikeAndPasswordLikeAndUsernameLikeAndStatus("%%", "", "", "", true, pageable);
				
				setRowCount((int) pageUsuario.getTotalElements());
				return datasource = pageUsuario.getContent();
			}
		};
	}
	
	public void iniciarLazyComisiones() {
		lstComisionesLazy = new LazyDataModel<Comisiones>() {
			private List<Comisiones> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Comisiones getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Comisiones comisiones : datasource) {
                    if (comisiones.getId() == intRowKey) {
                        return comisiones;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Comisiones comisiones) {
                return String.valueOf(comisiones.getId());
            }

			@Override
			public List<Comisiones> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {				
				
                Sort sort=Sort.by("lote.fechaVendido").ascending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	    System.out.println(entry.getKey() + "/" + entry.getValue());
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                	   }
                	}
                }
                
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
                
				Page<Comisiones> pageComisiones = null;
				if(opcionAsesor.equals("")) {
					pageComisiones= comisionesService.findByEstadoAndLoteStatusAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(true,EstadoLote.VENDIDO.getName(), "%%", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);

				}else if(opcionAsesor.equals("I")) {
					pageComisiones = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "I", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);
				}else if(opcionAsesor.equals("E")) {
					pageComisiones = comisionesService.findByEstadoAndLoteStatusAndTipoEmpleadoAndLoteFechaVendidoBetween(true, EstadoLote.VENDIDO.getName(), "E", comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);

				}else {
					Person personaSupervisor = new Person();
					int id = Integer.parseInt(opcionAsesor) ;
					personaSupervisor.setId(id);
					pageComisiones= comisionesService.findByEstadoAndLoteStatusAndLotePersonSupervisorAndLotePersonAssessorDniLikeAndLoteFechaVendidoBetween(true,EstadoLote.VENDIDO.getName(), personaSupervisor , "%%" , comisionSelected.getFechaIni(), comisionSelected.getFechaCierre(), pageable);

				}
				setRowCount((int) pageComisiones.getTotalElements());
				return datasource = pageComisiones.getContent();
			}
		};
	}
	
	public Converter getConversorComision() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                    Comision c = null;
                    for (Comision si : lstComision) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Comision) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorPersonSupervisor() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Person c = null;
                    for (Person si : lstPersonSupervisor) {
                        if (si.getId().toString().equals(value)) {
                            c = si;
                        }
                    }
                    return c;
                }
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                if (value == null || value.equals("")) {
                    return "";
                } else {
                    return ((Person) value).getId() + "";
                }
            }
        };
    }
	
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Integer getComisionContado() {
		return comisionContado;
	}
	public void setComisionContado(Integer comisionContado) {
		this.comisionContado = comisionContado;
	}
	public Integer getComisionCredito() {
		return comisionCredito;
	}
	public void setComisionCredito(Integer comisionCredito) {
		this.comisionCredito = comisionCredito;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public SimpleDateFormat getSdfM() {
		return sdfM;
	}
	public void setSdfM(SimpleDateFormat sdfM) {
		this.sdfM = sdfM;
	}
	public SimpleDateFormat getSdfY() {
		return sdfY;
	}
	public void setSdfY(SimpleDateFormat sdfY) {
		this.sdfY = sdfY;
	}
	public Team getTeamSelected() {
		return teamSelected;
	}
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}
	public TeamService getTeamService() {
		return teamService;
	}
	public void setTeamService(TeamService teamService) {
		this.teamService = teamService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public ComisionService getComisionService() {
		return comisionService;
	}
	public void setComisionService(ComisionService comisionService) {
		this.comisionService = comisionService;
	}
	public List<Comision> getLstComision() {
		return lstComision;
	}
	public void setLstComision(List<Comision> lstComision) {
		this.lstComision = lstComision;
	}
	public Comision getComisionSelected() {
		return comisionSelected;
	}
	public void setComisionSelected(Comision comisionSelected) {
		this.comisionSelected = comisionSelected;
	}

	public LazyDataModel<Comisiones> getLstComisionesLazy() {
		return lstComisionesLazy;
	}
	public void setLstComisionesLazy(LazyDataModel<Comisiones> lstComisionesLazy) {
		this.lstComisionesLazy = lstComisionesLazy;
	}

	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public EmpleadoService getEmpleadoService() {
		return empleadoService;
	}
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	public LazyDataModel<Usuario> getLstUsuarioLazy() {
		return lstUsuarioLazy;
	}
	public void setLstUsuarioLazy(LazyDataModel<Usuario> lstUsuarioLazy) {
		this.lstUsuarioLazy = lstUsuarioLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getTotalSolesContado() {
		return totalSolesContado;
	}
	public void setTotalSolesContado(double totalSolesContado) {
		this.totalSolesContado = totalSolesContado;
	}
	public double getTotalSolesInicial() {
		return totalSolesInicial;
	}
	public void setTotalSolesInicial(double totalSolesInicial) {
		this.totalSolesInicial = totalSolesInicial;
	}
	public double getTotalSolesPendiente() {
		return totalSolesPendiente;
	}
	public void setTotalSolesPendiente(double totalSolesPendiente) {
		this.totalSolesPendiente = totalSolesPendiente;
	}
//	public LazyDataModel<Lote> getLstLoteVendidoLazy() {
//		return lstLoteVendidoLazy;
//	}
//	public void setLstLoteVendidoLazy(LazyDataModel<Lote> lstLoteVendidoLazy) {
//		this.lstLoteVendidoLazy = lstLoteVendidoLazy;
//	}
	public Lote getLoteSelected() {
		return loteSelected;
	}
	public void setLoteSelected(Lote loteSelected) {
		this.loteSelected = loteSelected;
	}
	public List<Lote> getLstLotesVendidos() {
		return lstLotesVendidos;
	}
	public void setLstLotesVendidos(List<Lote> lstLotesVendidos) {
		this.lstLotesVendidos = lstLotesVendidos;
	}
	public List<Comisiones> getLstComisiones() {
		return lstComisiones;
	}
	public void setLstComisiones(List<Comisiones> lstComisiones) {
		this.lstComisiones = lstComisiones;
	}
	public PersonService getPersonService() {
		return personService;
	}
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	public List<Meta> getLstMeta() {
		return lstMeta;
	}
	public void setLstMeta(List<Meta> lstMeta) {
		this.lstMeta = lstMeta;
	}
	public ComisionesService getComisionesService() {
		return comisionesService;
	}
	public void setComisionesService(ComisionesService comisionesService) {
		this.comisionesService = comisionesService;
	}
	public List<Person> getLstPersonSupervisor() {
		return lstPersonSupervisor;
	}
	public void setLstPersonSupervisor(List<Person> lstPersonSupervisor) {
		this.lstPersonSupervisor = lstPersonSupervisor;
	}
	public Comisiones getComisionesSelected() {
		return comisionesSelected;
	}
	public void setComisionesSelected(Comisiones comisionesSelected) {
		this.comisionesSelected = comisionesSelected;
	}
	public String getOpcionAsesor() {
		return opcionAsesor;
	}
	public void setOpcionAsesor(String opcionAsesor) {
		this.opcionAsesor = opcionAsesor;
	}
	public MetaSupervisorService getMetaSupervisorService() {
		return metaSupervisorService;
	}
	public void setMetaSupervisorService(MetaSupervisorService metaSupervisorService) {
		this.metaSupervisorService = metaSupervisorService;
	}
	
}
