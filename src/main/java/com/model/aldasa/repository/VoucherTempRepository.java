package com.model.aldasa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.model.aldasa.entity.CuentaBancaria;
import com.model.aldasa.entity.PlantillaVenta;
import com.model.aldasa.entity.RequerimientoSeparacion;
import com.model.aldasa.entity.VoucherTemp;


public interface VoucherTempRepository extends PagingAndSortingRepository<VoucherTemp, Integer> {
	VoucherTemp findByPlantillaVentaAndMontoAndTipoTransaccionAndNumeroTransaccionAndFechaAndCuentaBancaria(PlantillaVenta plantilla, BigDecimal monto, String tipoTransaccion, String numTransaccion, Date fecha, CuentaBancaria cuentaBancaria);

	
	List<VoucherTemp> findByRequerimientoSeparacionAndEstado(RequerimientoSeparacion req, boolean status);
	List<VoucherTemp> findByPlantillaVentaAndEstado(PlantillaVenta plantilla, boolean status);

}
