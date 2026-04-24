package com.susana.citas.services;

import com.susana.citas.dto.CitaResponse;
import com.susana.citas.dto.CitaRequest;

import com.susana.commons.services.CrudService;

public interface CitaService extends CrudService <CitaRequest, CitaResponse>{
	
	
	void actualizarEstadoCita(Long idCita, Long EstadoCita);
	
	
	void medicoTieneCitasAsignadas(Long idMedico);
	
	void pacienteTieneCitasAsignadas(Long idPaciente);
	

}
