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
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Team;
import com.model.aldasa.entity.Usuario;
import com.model.aldasa.service.ComisionService;
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
	
	private LazyDataModel<Lote> lstLoteLazy;
	private LazyDataModel<Team> lstTeamLazy;
	private LazyDataModel<Usuario> lstUsuarioLazy;
	
	private Team teamSelected;
	private Person personAsesorSelected;
	private Comision comisionSelected;
	
	private Date fechaIni,fechaFin;
	private Integer comisionContado=8;
	private Integer comisionCredito=4;
	
	private boolean metaEquipo;
	
	
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
	
	public void verDetalleLotes() {
		iniciarLazyLotes();
		double metaPorEquipo = calcularProcentajeMeta(teamSelected, "NO");
		metaEquipo = false;
		if(metaPorEquipo >= 100) {
			metaEquipo = true;
		}
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
				
				for(Lote lote : lstLotes) {
					if(asesor.equals(lote.getPersonAssessor())) {
						int nro = asesor.getLotesVendidos()+1;
						asesor.setLotesVendidos(nro); 
						
					}
				}
			}
			
		}
		
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
	
	public void iniciarLazyLotes() {
		lstLoteLazy = new LazyDataModel<Lote>() {
			private List<Lote> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Lote getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Lote lote : datasource) {
                    if (lote.getId() == intRowKey) {
                        return lote;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Lote lote) {
                return String.valueOf(lote.getId());
            }

			@Override
			public List<Lote> load(int first, int pageSize, Map<String, SortMeta> sortBy,Map<String, FilterMeta> filterBy) {
				
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
                
				Page<Lote> pageLote;
				pageLote= loteService.findAllByStatusLikeAndPersonSupervisorDniLikeAndPersonAssessorDniLikeAndFechaVendidoBetween(status,dniSupervisor,dniAsesor,fechaIni,fechaFin, pageable);
				
				setRowCount((int) pageLote.getTotalElements());
				return datasource = pageLote.getContent();
			}
		};
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
	public LazyDataModel<Lote> getLstLoteLazy() {
		return lstLoteLazy;
	}
	public void setLstLoteLazy(LazyDataModel<Lote> lstLoteLazy) {
		this.lstLoteLazy = lstLoteLazy;
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
	
}
