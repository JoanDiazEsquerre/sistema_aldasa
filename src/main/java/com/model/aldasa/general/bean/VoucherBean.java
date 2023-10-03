package com.model.aldasa.general.bean;


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.eclipse.jdt.internal.compiler.env.IUpdatableModule.AddExports;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.Imagen;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.entity.Team;
import com.model.aldasa.service.CuentaBancariaService;
import com.model.aldasa.service.ImagenService;
import com.model.aldasa.service.LoteService;
import com.model.aldasa.service.ManzanaService;
import com.model.aldasa.service.PersonService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.SucursalService;
import com.model.aldasa.service.TeamService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class VoucherBean extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	@ManagedProperty(value = "#{imagenService}")
	private ImagenService imagenService;
	
	@ManagedProperty(value = "#{projectService}")
	private ProjectService projectService;
	
	@ManagedProperty(value = "#{manzanaService}")
	private ManzanaService manzanaService;
	
	@ManagedProperty(value = "#{loteService}")
	private LoteService loteService;
	
	@ManagedProperty(value = "#{cuentaBancariaService}")
	private CuentaBancariaService cuentaBancariaService;
	
	@ManagedProperty(value = "#{sucursalService}")
	private SucursalService sucursalService;
	
	private boolean estado;
	private String tituloDialog;
	
	private Sucursal sucursal, sucursalDialog;
	private Imagen imagenSelected;
	
	private List<Sucursal> lstSucursal;
	private List<CuentaBancaria> lstCuentaBancaria;
	
	private LazyDataModel<Imagen> lstImagenLazy;
	
	SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	
	@PostConstruct
	public void init() {
		estado=true;
		lstSucursal =sucursalService.findByEstado(true);
		iniciarLazy();
	}
	
	public void saveVoucher() {
		if(imagenSelected.getFecha() == null) {
			addErrorMessage("Debe ingresar una fecha.");
			return;
		}
		if(imagenSelected.getMonto() == null) {
			addErrorMessage("Debe ingresar un monto.");
			return;
		}else {
			if(imagenSelected.getMonto().compareTo(BigDecimal.ZERO) < 1) {
				addErrorMessage("El monto debe ser mayor que 0.");
				return;
			}
		}
		if(imagenSelected.getNumeroOperacion().equals("")) {
			addErrorMessage("Debe ingresar número de operación.");
			return;
		}
		
		if(imagenSelected.getCuentaBancaria()==null) {
			addErrorMessage("Debe seleccionar una cuenta bancaria.");
			return;
		}
		
		
		imagenSelected.setUsuario(navegacionBean.getUsuarioLogin());
		imagenSelected.setFechaRegistro(new Date());
		
		List<Imagen> buscarImagen = imagenService.findByEstadoAndFechaAndMontoAndNumeroOperacionAndCuentaBancariaAndTipoTransaccion(true, imagenSelected.getFecha(), imagenSelected.getMonto(), imagenSelected.getNumeroOperacion(), imagenSelected.getCuentaBancaria(), imagenSelected.getTipoTransaccion());
		if(!buscarImagen.isEmpty()) {
			addErrorMessage("Ya existe el voucher");
			return;
		}
		
		Imagen imagen = imagenService.save(imagenSelected);
		if(imagen != null) {
			addInfoMessage("Se registró correctamente el voucher.");
			PrimeFaces.current().executeScript("PF('imagenDialog').hide();"); 

		}else {
			addErrorMessage("No se pudo guardar el voucher"); 
		}
	}
	
	public void listarCuentaBancaria() {
		lstCuentaBancaria = new ArrayList<>();
		
		if(sucursalDialog!= null) {
			lstCuentaBancaria=cuentaBancariaService.findByEstadoAndSucursal(true, sucursalDialog);
			if(!lstCuentaBancaria.isEmpty()) {
				imagenSelected.setCuentaBancaria(lstCuentaBancaria.get(0));
			}
		}
		
	}
	
	public void anularVoucher() {
		imagenSelected.setEstado(false);
		imagenService.save(imagenSelected);
		addInfoMessage("Se anuló correctamente el voucher.");
	}
	
	public void newImagen() {
		tituloDialog="NUEVO VOUCHER";
		sucursalDialog=null;
		lstCuentaBancaria = new ArrayList<>();
		imagenSelected = new Imagen();
		imagenSelected.setNombre("-");
		imagenSelected.setCarpeta("IMG-DOCUMENTO-VENTA");
		imagenSelected.setEstado(true);
		imagenSelected.setTipoTransaccion("DEPOSITO EN EFECTIVO");
		
	}
	
	public String convertirHora(Date hora) {
		String a = sdfFull.format(hora);
		return a;
	}
	
	public void iniciarLazy() {

		lstImagenLazy = new LazyDataModel<Imagen>() {
			private List<Imagen> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public Imagen getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (Imagen i : datasource) {
                    if (i.getId() == intRowKey) {
                        return i;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(Imagen imagen) {
                return String.valueOf(imagen.getId());
            }

			@Override
			public List<Imagen> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
               
				String numeroOp = "%" + (filterBy.get("numeroOperacion") != null ? filterBy.get("numeroOperacion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String cuenta = "%" + (filterBy.get("cuentaBancaria.numero") != null ? filterBy.get("cuentaBancaria.numero").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				String tipoTransaccion = "%" + (filterBy.get("tipoTransaccion") != null ? filterBy.get("tipoTransaccion").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
 
				
				
                Sort sort=Sort.by("id").descending();
                if(sortBy!=null) {
                	for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
                	   if(entry.getValue().getOrder().isAscending()) {
                		   sort = Sort.by(entry.getKey()).descending();
                	   }else {
                		   sort = Sort.by(entry.getKey()).ascending();
                		   
                	   }
                	}
                }          
                Pageable pageable = PageRequest.of(first/pageSize, pageSize,sort);
               
                Page<Imagen> page=null;
                String sucursalName="%%";
                if(sucursal!=null) {
                	sucursalName="%"+sucursal.getRazonSocial()+"%";
                }
               
                
                page =  imagenService.findByEstadoAndCuentaBancariaSucursalRazonSocialLikeAndNumeroOperacionLikeAndCuentaBancariaNumeroLikeAndTipoTransaccionLikeOrderByIdDesc(estado,sucursalName,numeroOp,cuenta,tipoTransaccion, pageable); 
                
                setRowCount((int) page.getTotalElements());
                return datasource = page.getContent();
            }
		};
	}
	
	public Converter getConversorSucursal() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Sucursal c = null;
                    for (Sucursal si : lstSucursal) {
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
                    return ((Sucursal) value).getId() + "";
                }
            }
        };
    }
	
	public Converter getConversorCuentaBancaria() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	CuentaBancaria c = null;
                    for (CuentaBancaria si : lstCuentaBancaria) {
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
                    return ((CuentaBancaria) value).getId() + "";
                }
            }
        };
    }

	public ImagenService getImagenService() {
		return imagenService;
	}
	public void setImagenService(ImagenService imagenService) {
		this.imagenService = imagenService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	public ManzanaService getManzanaService() {
		return manzanaService;
	}
	public void setManzanaService(ManzanaService manzanaService) {
		this.manzanaService = manzanaService;
	}
	public LoteService getLoteService() {
		return loteService;
	}
	public void setLoteService(LoteService loteService) {
		this.loteService = loteService;
	}
	public CuentaBancariaService getCuentaBancariaService() {
		return cuentaBancariaService;
	}
	public void setCuentaBancariaService(CuentaBancariaService cuentaBancariaService) {
		this.cuentaBancariaService = cuentaBancariaService;
	}
	public SucursalService getSucursalService() {
		return sucursalService;
	}
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<Sucursal> getLstSucursal() {
		return lstSucursal;
	}
	public void setLstSucursal(List<Sucursal> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}
	public Imagen getImagenSelected() {
		return imagenSelected;
	}
	public void setImagenSelected(Imagen imagenSelected) {
		this.imagenSelected = imagenSelected;
	}
	public LazyDataModel<Imagen> getLstImagenLazy() {
		return lstImagenLazy;
	}
	public void setLstImagenLazy(LazyDataModel<Imagen> lstImagenLazy) {
		this.lstImagenLazy = lstImagenLazy;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SimpleDateFormat getSdfFull() {
		return sdfFull;
	}
	public void setSdfFull(SimpleDateFormat sdfFull) {
		this.sdfFull = sdfFull;
	}
	public Sucursal getSucursalDialog() {
		return sucursalDialog;
	}
	public void setSucursalDialog(Sucursal sucursalDialog) {
		this.sucursalDialog = sucursalDialog;
	}
	public List<CuentaBancaria> getLstCuentaBancaria() {
		return lstCuentaBancaria;
	}
	public void setLstCuentaBancaria(List<CuentaBancaria> lstCuentaBancaria) {
		this.lstCuentaBancaria = lstCuentaBancaria;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	
	
		
}
