package com.model.aldasa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.model.aldasa.entity.Comision;
import com.model.aldasa.entity.Empleado;;

public interface ComisionService {
	
	Optional<Comision> findById(Integer id);
	Comision save(Comision entity);
	void delete(Comision entity);
	
	List<Comision> findByEstado(Boolean estado);
	Comision findByEstadoAndCodigo(Boolean estado, String codigo);
	Comision findByCodigoAndIdException(String codigo, int idComision);


	Page<Comision> findByEstadoAndFechaIniBetween(Boolean estado,Date fechaIni, Date fechaFin, Pageable pageable);

}
