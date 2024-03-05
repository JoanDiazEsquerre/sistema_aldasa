package com.model.aldasa.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.model.aldasa.entity.ComisionProyecto;
import com.model.aldasa.entity.DetalleComisiones;
import com.model.aldasa.entity.Contrato;
import com.model.aldasa.entity.Lote;
import com.model.aldasa.entity.MetaSupervisor;
import com.model.aldasa.entity.Person;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.Project;
import com.model.aldasa.entity.Sucursal;
import com.model.aldasa.repository.ComisionProyectoRepository;
//import com.model.aldasa.repository.DetalleComisionesRepository;
import com.model.aldasa.repository.ContratoRepository;
import com.model.aldasa.repository.PlantillaVentaRepository;
import com.model.aldasa.service.ContratoService;
import com.model.aldasa.util.EstadoLote;

@Service("contratoService")
public class ContratoServiceImpl implements ContratoService{
	
	@Autowired
	private ContratoRepository contratoRepository;
	
//	@Autowired
//	private ComisionRepository comisionRepository;
	
//	@Autowired
//	private DetalleComisionesRepository comisionesRepository;
	
	@Autowired
	private PlantillaVentaRepository plantillaVentaRepository;
	
	@Autowired
	private ComisionProyectoRepository comisionProyectoRepository;

	@Override
	public Optional<Contrato> findById(Integer id) {
		return contratoRepository.findById(id);
	}

	@Override
	public Contrato saveGeneraComision(Contrato entity) {
//		if(entity.isFirma()) {
//			Comision comision = comisionRepository.findByFechaIniLessThanEqualAndFechaCierreGreaterThanEqual(entity.getFechaVenta(), entity.getFechaVenta());
//			if(comision != null){
//				ComisionProyecto cp = comisionProyectoRepository.findByComisionAndProyectoAndEstado(comision, entity.getLote().getProject(), true);
//				BigDecimal interesContado=new BigDecimal(comision.getComisionContado()); BigDecimal interesCredito=new BigDecimal(comision.getComisionCredito());
//				if(cp!=null) {
//					interesContado = cp.getInteresContado();
//					interesCredito = cp.getInteresCredito();
//				}
//			
//				
//				List<PlantillaVenta> lstPlantilla = plantillaVentaRepository.findByEstadoAndLote("Aprobado", entity.getLote());
//				if(!lstPlantilla.isEmpty()) {
//					PlantillaVenta plantilla = lstPlantilla.get(0);
//					Comisiones comisiones = new Comisiones();
//					comisiones.setLote(entity.getLote());
//					comisiones.setPersonAsesor(plantilla.getPersonAsesor());
//					comisiones.setPersonSupervisor(plantilla.getPersonSupervisor());
//					
//					if (entity.getTipoPago().equals("Contado")) {
//						BigDecimal multiplica = entity.getMontoVenta().multiply(interesContado);
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));	
//					}else {
//						BigDecimal multiplica = entity.getMontoVenta().multiply(interesCredito);
//						comisiones.setComisionAsesor(multiplica.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					}
//					
//					BigDecimal multiplicaSup = entity.getMontoVenta().multiply(new BigDecimal(comision.getComisionSupervisor()));
//					comisiones.setComisionSupervisor(multiplicaSup.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					
//					BigDecimal multiplicaSub = entity.getMontoVenta().multiply(new BigDecimal(comision.getSubgerente()));
//					comisiones.setComisionSubgerente(multiplicaSub.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//					comisiones.setEstado(true);
//					comisiones.setComision(comision); 
//					comisiones.setContrato(entity);
//					comisionesRepository.save(comisiones);
//					entity.setComisiones(comisiones);
//				}
//			}
//		}else {
//			if(entity.getComisiones() != null) {
//				entity.getComisiones().setEstado(false);
//				comisionesRepository.save(entity.getComisiones());
//			}
//			
//			entity.setComisiones(null);
//		}
		
//		return contratoRepository.save(entity);
		return null;
	}

	@Override
	public void delete(Contrato entity) {
		contratoRepository.delete(entity); 
	}

	@Override
	public List<Contrato> findAll() {
		// TODO Auto-generated method stub
		return (List<Contrato>) contratoRepository.findAll();
	}

	@Override
	public Page<Contrato> findByEstado(boolean status, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstado(status, pageable);
	}

	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status, Sucursal sucursal,Project project, String manzana, String numeroLote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteProjectAndLoteManzanaNameLikeAndLoteNumberLoteLike(status, sucursal, project, manzana, numeroLote, pageable);
	}

	@Override
	public Page<Contrato> findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(
			String personVenta, String dni, boolean estado, boolean cancelacionTotal, Sucursal sucursal,Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByPersonVentaSurnamesLikeAndPersonVentaDniLikeAndEstadoAndCancelacionTotalAndLoteProjectSucursal(personVenta, dni, estado, cancelacionTotal, sucursal, pageable);
	}

	@Override
	public List<Contrato> findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(boolean status, Sucursal sucursal, String tipoPago, boolean cancelacionTotal) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndTipoPagoAndCancelacionTotal(status, sucursal, tipoPago, cancelacionTotal); 
	}

	@Override
	public Contrato save(Contrato entity) {
		// TODO Auto-generated method stub
		return contratoRepository.save(entity);
	}

	@Override
	public Page<Contrato> findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLike(boolean status,
			Sucursal sucursal, String manzana, String numLote, Pageable pageable) {
		// TODO Auto-generated method stub
		return contratoRepository.findByEstadoAndLoteProjectSucursalAndLoteManzanaNameLikeAndLoteNumberLoteLike(status, sucursal, manzana, numLote, pageable);
	}

	@Override
	public Contrato findByLoteAndEstado(Lote lote, boolean estado) {
		// TODO Auto-generated method stub
		return contratoRepository.findByLoteAndEstado(lote, estado);
	}



	


}
