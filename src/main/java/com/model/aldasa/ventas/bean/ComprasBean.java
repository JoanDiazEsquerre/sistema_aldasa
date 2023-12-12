package com.model.aldasa.ventas.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
import javax.servlet.ServletContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.model.aldasa.entity.Caja;
import com.model.aldasa.entity.Cuota;
import com.model.aldasa.entity.DetalleCaja;
import com.model.aldasa.entity.DetalleDocumentoVenta;
import com.model.aldasa.entity.DetalleOrdenCompra;
import com.model.aldasa.entity.OrdenCompra;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Unidad;
import com.model.aldasa.general.bean.NavegacionBean;
import com.model.aldasa.service.CajaService;
import com.model.aldasa.service.DetalleCajaService;
import com.model.aldasa.service.DetalleOrdenCompraService;
import com.model.aldasa.service.OrdenCompraService;
import com.model.aldasa.service.ProjectService;
import com.model.aldasa.service.SerieDocumentoService;
import com.model.aldasa.service.UnidadService;
import com.model.aldasa.util.BaseBean;
import com.model.aldasa.util.UtilXls;

@ManagedBean
@ViewScoped
public class ComprasBean extends BaseBean {
	
	@ManagedProperty(value = "#{detalleOrdenCompraService}")
	private DetalleOrdenCompraService detalleOrdenCompraService;
	
	@ManagedProperty(value = "#{ordenCompraService}")
	private OrdenCompraService ordenCompraService;
	
	@ManagedProperty(value = "#{unidadService}")
	private UnidadService unidadService;
	
	private static final long serialVersionUID = 1L;
	
	private LazyDataModel<OrdenCompra> lstOrdenCompraLazy;
	
	private List<DetalleOrdenCompra>  lstDetalleOrdenCompra;
	private List<Unidad> lstUnidad = new ArrayList<>();


	private OrdenCompra ordenCompraSelected;
	private DetalleOrdenCompra detalleOrdenCompraSelected;
	private Unidad unidadFilter;
	
	private String cantidad, descripcionProducto;
	private Date fecha;
	private BigDecimal precio, total;

	private String estado = "Pendiente" ;
	
	@PostConstruct
	public void init() {
		iniciarLazy();
		lstUnidad= unidadService.findByEstado(true);
	}
	
	public void deleteDetalle(DetalleOrdenCompra detalle) {
		lstDetalleOrdenCompra.remove(detalle);
		addInfoMessage("Detalle eliminado correctamente.");
	}
	
	public void iniciarLazy() {

		lstOrdenCompraLazy = new LazyDataModel<OrdenCompra>() {
			private List<OrdenCompra> datasource;

            @Override
            public void setRowIndex(int rowIndex) {
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex % getPageSize());
                }
            }

            @Override
            public OrdenCompra getRowData(String rowKey) {
                int intRowKey = Integer.parseInt(rowKey);
                for (OrdenCompra ordenCompra : datasource) {
                    if (ordenCompra.getId() == intRowKey) {
                        return ordenCompra;
                    }
                }
                return null;
            }

            @Override
            public String getRowKey(OrdenCompra ordenCompra) {
                return String.valueOf(ordenCompra.getId());
            }

			@Override
			public List<OrdenCompra> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
//				String names = "%" + (filterBy.get("contrato.personVenta.surnames") != null ? filterBy.get("contrato.personVenta.surnames").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
//				String dni = "%" + (filterBy.get("contrato.personVenta.dni") != null ? filterBy.get("contrato.personVenta.dni").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
//				String numLote = "%" + (filterBy.get("contrato.lote.numberLote") != null ? filterBy.get("contrato.lote.numberLote").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
//				String manzana = "%" + (filterBy.get("contrato.lote.manzana.name") != null ? filterBy.get("contrato.lote.manzana.name").getFilterValue().toString().trim().replaceAll(" ", "%") : "") + "%";
				
				
                Sort sort=Sort.by("id").ascending();
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
               
                Page<OrdenCompra> pageOrdenCompra= ordenCompraService.findByEstado(estado, pageable);

				
                setRowCount((int) pageOrdenCompra.getTotalElements());
                return datasource = pageOrdenCompra.getContent();
            }
		};
	}

	public void agregarItem() {
		if(cantidad.equals("")) {
			addErrorMessage("Ingresar cantidad.");
			return;
		}
		if(descripcionProducto.equals("")) {
			addErrorMessage("Ingresar descripción del producto.");
			return;
		}
		if(precio==null) {
			addErrorMessage("Ingresar precio.");
			return;
		}
		if(total==null) {
			addErrorMessage("Ingresar total.");
			return;
		}
		
		DetalleOrdenCompra detalle = new DetalleOrdenCompra();
		detalle.setCantidad(cantidad);
		detalle.setDescripcionProducto(descripcionProducto);
		detalle.setUnidad(unidadFilter);
		detalle.setEstado(true);
		detalle.setPrecio(precio);
		detalle.setTotal(total);
		lstDetalleOrdenCompra.add(detalle);
		cantidad = "";
		descripcionProducto="";
		precio = null;
		total = null;
		addInfoMessage("Item Agregado.");

		
		
//		DetalleOrdenCompra d = detalleOrdenCompraService.save(detalle);
//		if(d==null) {
//			addErrorMessage("No se guardó correctamente.");
//			return;
//		}else {
//			addInfoMessage("Se guardó correctamente.");
//		}
		
	}
	
	public void saveCompra() {
		
		
	}
	
	public Converter getConversorUnidad() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value.trim().equals("") || value == null || value.trim().equals("null")) {
                    return null;
                } else {
                	Unidad c = null;
                    for (Unidad si : lstUnidad) {
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
                    return ((Unidad) value).getId() + "";
                }
            }
        };
    }
	
	
	
	
	
	
	public LazyDataModel<OrdenCompra> getLstOrdenCompraLazy() {
		return lstOrdenCompraLazy;
	}
	public void setLstOrdenCompraLazy(LazyDataModel<OrdenCompra> lstOrdenCompraLazy) {
		this.lstOrdenCompraLazy = lstOrdenCompraLazy;
	}
	public OrdenCompra getOrdenCompraSelected() {
		return ordenCompraSelected;
	}
	public void setOrdenCompraSelected(OrdenCompra ordenCompraSelected) {
		this.ordenCompraSelected = ordenCompraSelected;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<DetalleOrdenCompra> getLstDetalleOrdenCompra() {
		return lstDetalleOrdenCompra;
	}
	public void setLstDetalleOrdenCompra(List<DetalleOrdenCompra> lstDetalleOrdenCompra) {
		this.lstDetalleOrdenCompra = lstDetalleOrdenCompra;
	}
	public DetalleOrdenCompra getDetalleOrdenCompraSelected() {
		return detalleOrdenCompraSelected;
	}
	public void setDetalleOrdenCompraSelected(DetalleOrdenCompra detalleOrdenCompraSelected) {
		this.detalleOrdenCompraSelected = detalleOrdenCompraSelected;
	}
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getDescripcionProducto() {
		return descripcionProducto;
	}
	public void setDescripcionProducto(String descripcionProducto) {
		this.descripcionProducto = descripcionProducto;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public DetalleOrdenCompraService getDetalleOrdenCompraService() {
		return detalleOrdenCompraService;
	}
	public void setDetalleOrdenCompraService(DetalleOrdenCompraService detalleOrdenCompraService) {
		this.detalleOrdenCompraService = detalleOrdenCompraService;
	}
	public OrdenCompraService getOrdenCompraService() {
		return ordenCompraService;
	}
	public void setOrdenCompraService(OrdenCompraService ordenCompraService) {
		this.ordenCompraService = ordenCompraService;
	}
	public Unidad getUnidadFilter() {
		return unidadFilter;
	}
	public void setUnidadFilter(Unidad unidadFilter) {
		this.unidadFilter = unidadFilter;
	}
	public UnidadService getUnidadService() {
		return unidadService;
	}
	public void setUnidadService(UnidadService unidadService) {
		this.unidadService = unidadService;
	}
	public List<Unidad> getLstUnidad() {
		return lstUnidad;
	}
	public void setLstUnidad(List<Unidad> lstUnidad) {
		this.lstUnidad = lstUnidad;
	}
	
	
}
