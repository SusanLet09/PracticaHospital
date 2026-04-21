package com.susana.pacientes.services;

import com.susana.commons.dto.PacienteRequest;
import com.susana.commons.dto.PacienteResponse;
import com.susana.commons.services.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {
	
	
	PacienteResponse obtenerPacientePorIdSinEstado(Long id);
	
}
