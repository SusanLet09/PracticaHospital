package com.susana.medicos.services;

import com.susana.commons.dto.MedicoRequest;
import com.susana.commons.dto.MedicoResponse;
import com.susana.commons.services.CrudService;


public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
	
MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
	
	void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);
	
}
