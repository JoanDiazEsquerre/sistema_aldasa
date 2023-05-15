package com.model.aldasa.almacen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.Distribucion;
import com.model.aldasa.entity.Inventario;
import com.model.aldasa.entity.Person;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.InventarioService;
import com.model.aldasa.util.BaseBean;

@ManagedBean
@ViewScoped
public class InventarioBean extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = "#{inventarioService}")
	private InventarioService inventarioService;
	
	@ManagedProperty(value = "#{navegacionBean}")
	private NavegacionBean navegacionBean;
	
	private LazyDataModel<Inventario> lstInventarioLazy;
	private List<Distribucion> lstDistribucion = new ArrayList<>();
	
	private Boolean estado = true;
	private String tituloDialog;
	
	private Inventario inventarioSelected;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
	}

	public void newInventario() {
		tituloDialog = "NUEVO INVENTARIO";
		inventarioSelected = new Inventario();
		inventarioSelected.setEstado(true);
		inventarioSelected.setSucursal(navegacionBean.getSucursalLogin());
	}
	
	public void updateInventario() {
		tituloDialog = "MODIFICAR INVENTARIO";
	}
	
	public void saveInventario() {
		
		if(inventarioSelected.getNombre().equals("")) {
			addErrorMessage("Ingresar nombre.");
			return;
		}
		if(inventarioSelected.getModelo().equals("")) {
			addErrorMessage("Ingresar modelo.");
			return;
		}
		if(inventarioSelected.getMarca().equals("")) {
			addErrorMessage("Ingresar marca.");
			return;
		}
		if(inventarioSelected.getCantidad() == null ) {
			addErrorMessage("Ingresar cantidad.");
			return;
		}else if(inventarioSelected.getCantidad() <= 0) {
			addErrorMessage("Cantidad debe ser mayor que 0.");
			return;
		}
		
		Inventario inv = inventarioService.save(inventarioSelected);
		if(inv == null) {
			addErrorMessage("No se pudo guardar.");
		}else {
			addInfoMessage("Se guardó correctamente.");
		}
		
		
	}

	
	
	public void iniciarLazy() {
		lstInventarioLazy = new LazyDataModel<Inventario>() {
			private List<Inventario> datasource;

			@Override
			public void setRowIndex(int rowIndex) {
				if (rowIndex == -1 || getPageSize() == 0) {
					super.setRowIndex(-1);
				} else {
					super.setRowIndex(rowIndex % getPageSize());
				}
			}

			@Override
			public Inventario getRowData(String rowKey) {
				int intRowKey = Integer.parseInt(rowKey);
				for (Inventario inventario : datasource) {
					if (inventario.getId() == intRowKey) {
						return inventario;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Inventario inventario) {
				return String.valueOf(inventario.getId());
			}

			@Override
			public List<Inventario> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Sort sort = Sort.by("estado").ascending();
				if (sortBy != null) {
					for (Map.Entry<String, SortMeta> entry : sortBy.entrySet()) {
						System.out.println(entry.getKey() + "/" + entry.getValue());
						if (entry.getValue().getOrder().isAscending()) {
							sort = Sort.by(entry.getKey()).descending();
						} else {
							sort = Sort.by(entry.getKey()).ascending();

						}

					}
				}

				Pageable pageable = PageRequest.of(first / pageSize, pageSize, sort);
		
				
				Page<Inventario> pageInventario = inventarioService.findByEstadoAndSucursalEmpresa(estado, navegacionBean.getSucursalLogin().getEmpresa(), pageable);
						
				setRowCount((int) pageInventario.getTotalElements());
				return datasource = pageInventario.getContent();
			}
		};
	}
	
	
	
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public LazyDataModel<Inventario> getLstInventarioLazy() {
		return lstInventarioLazy;
	}
	public void setLstInventarioLazy(LazyDataModel<Inventario> lstInventarioLazy) {
		this.lstInventarioLazy = lstInventarioLazy;
	}
	public InventarioService getInventarioService() {
		return inventarioService;
	}
	public void setInventarioService(InventarioService inventarioService) {
		this.inventarioService = inventarioService;
	}
	public Inventario getInventarioSelected() {
		return inventarioSelected;
	}
	public void setInventarioSelected(Inventario inventarioSelected) {
		this.inventarioSelected = inventarioSelected;
	}
	public String getTituloDialog() {
		return tituloDialog;
	}
	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}
	public NavegacionBean getNavegacionBean() {
		return navegacionBean;
	}
	public void setNavegacionBean(NavegacionBean navegacionBean) {
		this.navegacionBean = navegacionBean;
	}
	public List<Distribucion> getLstDistribucion() {
		return lstDistribucion;
	}
	public void setLstDistribucion(List<Distribucion> lstDistribucion) {
		this.lstDistribucion = lstDistribucion;
	}
	
}
