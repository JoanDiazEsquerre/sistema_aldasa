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
import com.model.aldasa.entity.Empleado;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ComisionService;
import com.model.aldasa.service.EmpleadoService;
import com.model.aldasa.service.LoteService;
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
	
	@ManagedProperty(value = "#{empleadoService}")
	private EmpleadoService empleadoService;
	
	private LazyDataModel<Team> lstTeamLazy;
	private LazyDataModel<Usuario> lstUsuarioLazy;
	
	private Team teamSelected;
	private Person personAsesorSelected;
	private Comision comisionSelected;
	
	private Date fechaIni,fechaFin;
	private Integer comisionContado=8;
	private Integer comisionCredito=4;
	
	private boolean metaEquipo;
	
	private double totalSolesContado = 0;
	private double totalSolesInicial = 0;
	
	
	private List<Person> lstPersonAsesor;
	private List<Comision> lstComision;
	
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
	SimpleDateFormat sdfM = new SimpleDateFormat("MM");  
	SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");  
	SimpleDateFormat sdfY2 = new SimpleDateFormat("yy"); 
	
	@PostConstruct
	public void init() {
		comisionSelected = comisionService.findByEstadoAndCodigo(true, sdfM.format(new Date())+sdfY2.format(new Date()));
		cambiarComision();
		lstComision = comisionService.findByEstado(true);
		
		iniciarLazyTeam();
	}
	
	public void verDetalleAsesores() {
		lstPersonAsesor = new ArrayList<>();
		List<Lote> lstLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), teamSelected.getPersonSupervisor(), "%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		int contador = 0;
		if(!lstLotes.isEmpty()) {
			for(Lote lote : lstLotes) {
				Person personAsesor = lote.getPersonAssessor();
				
				if(contador == 0) {
					lstPersonAsesor.add(personAsesor);
				}else {
					boolean encuentra = false;
					for(Person asesor:lstPersonAsesor) {
						if(asesor.equals(lote.getPersonAssessor())) {
							encuentra=true;
						}
					}
					
					if(!encuentra) {
						lstPersonAsesor.add(personAsesor);
					}
				}
				contador++;
			}
			
			for(Person asesor:lstPersonAsesor) {
				int nroContado = 0;
				int nroCredito = 0;
				double comisionContado = 0.0;
				double comisionCredito = 0.0;
				double totalComision = 0.0;
				
				for(Lote lote : lstLotes) {
					if(asesor.equals(lote.getPersonAssessor())) {
						int nro = asesor.getLotesVendidos()+1;
						if(lote.getTipoPago().equals("Contado")) {
							nroContado = nroContado+1;
							totalComision = totalComision + calcularComisionAsesor(lote);
							comisionContado = comisionContado+ calcularComisionAsesor(lote);
						}else {
							nroCredito = nroCredito+1;
							totalComision = totalComision + calcularComisionAsesor(lote);
							comisionCredito = comisionCredito+ calcularComisionAsesor(lote);
						}
						
						asesor.setLotesVendidos(nro); 
						asesor.setLotesContado(nroContado);
						asesor.setLotesCredito(nroCredito);
						asesor.setTotalComision(totalComision); 
						asesor.setComisionContado(comisionContado);
						asesor.setComisionCredito(comisionCredito); 
					}
				}
			}
		}
	}
	
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
	
	public int calcularProcentajeMeta(Team team, String size) {
		int porc=0;
		List<Lote> listLotesVendido = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(), "%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(listLotesVendido != null && !listLotesVendido.isEmpty()) {
			if(size.equals("SI")) {
				return listLotesVendido.size();
			}
			
			int sizeLotes = listLotesVendido.size();
			double calculo = (sizeLotes*100)/comisionSelected.getMeta();
			sizeLotes = (int) calculo;
			return sizeLotes;
		}
		return porc;
	}
	
	public void cambiarComision() {
		fechaIni = comisionSelected.getFechaIni();
		fechaFin = comisionSelected.getFechaCierre();
		
		totalSolesContado = 0;
		totalSolesInicial = 0;
	}
	
	public double calcularComisionSubgerente(Lote lote) {

		double comision = 0;
		double porcSubgerente =  Double.parseDouble(comisionSelected.getSubgerente()+"")  / 100;
		comision = lote.getMontoVenta() * porcSubgerente;

		return comision;
	}
	
	public double calcularComisionSupervior(Lote lote) {
		double comision = 0;
		double porcSupervisor = Double.parseDouble(comisionSelected.getComisionSupervisor()+"") / 100;
		double porcSupervisorMeta = Double.parseDouble(comisionSelected.getComisionMetaSupervisor()+"") / 100;
		
		if(metaEquipo) {
			comision = lote.getMontoVenta() * porcSupervisorMeta;
		}else {
			comision = lote.getMontoVenta() * porcSupervisor;
		}
		

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
		List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(!listaLotes.isEmpty()) {
			for(Lote lote : listaLotes){
				if(lote.getTipoPago().equals("Contado")) {
					solesContado = solesContado + lote.getMontoVenta();
				}
			}
		}
		totalSolesContado = totalSolesContado +solesContado;
		return solesContado;
	}
	
	public double calcularSolesInicial(Team team) {
		double solesCredito = 0;
		List<Lote> listaLotes = loteService.findByStatusAndPersonSupervisorAndPersonAssessorDniLikeAndFechaVendidoBetween(EstadoLote.VENDIDO.getName(), team.getPersonSupervisor(),"%%",comisionSelected.getFechaIni(), comisionSelected.getFechaCierre());
		if(!listaLotes.isEmpty()) {
			for(Lote lote : listaLotes){
				if(lote.getTipoPago().equals("Crédito")) {
					solesCredito = solesCredito + lote.getMontoInicial();
				}
			}
		}
		totalSolesInicial = totalSolesInicial + solesCredito;
		return solesCredito;
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
	
	public void iniciarLazyTeam() {
		lstTeamLazy = new LazyDataModel<Team>() {
			private List<Team> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Team getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Team team : datasource) {
                    if (team.getId() == intRowKey) {
                        return team;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Team team) {
                return String.valueOf(team.getId());
            }

			@Override
			public List<Team> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {				
				String name="%"+ (filterBy.get("name")!=null?filterBy.get("name").getFilterValue().toString().trim().replaceAll(" ", "%"):"")+ "%";
				
                Sort sort=Sort.by("name").ascending();
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
                
				Page<Team> pageTeam;
				pageTeam= teamService.findByNameLikeAndStatus(name, true, pageable);
				
				setRowCount((int) pageTeam.getTotalElements());
				return datasource = pageTeam.getContent();
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
	public Person getPersonAsesorSelected() {
		return personAsesorSelected;
	}
	public void setPersonAsesorSelected(Person personAsesorSelected) {
		this.personAsesorSelected = personAsesorSelected;
	}
	public List<Person> getLstPersonAsesor() {
		return lstPersonAsesor;
	}
	public void setLstPersonAsesor(List<Person> lstPersonAsesor) {
		this.lstPersonAsesor = lstPersonAsesor;
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
	public LazyDataModel<Team> getLstTeamLazy() {
		return lstTeamLazy;
	}
	public void setLstTeamLazy(LazyDataModel<Team> lstTeamLazy) {
		this.lstTeamLazy = lstTeamLazy;
	}
	public SimpleDateFormat getSdfY2() {
		return sdfY2;
	}
	public void setSdfY2(SimpleDateFormat sdfY2) {
		this.sdfY2 = sdfY2;
	}
	public boolean isMetaEquipo() {
		return metaEquipo;
	}
	public void setMetaEquipo(boolean metaEquipo) {
		this.metaEquipo = metaEquipo;
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
	
}
