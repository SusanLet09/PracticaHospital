package com.susana.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mvs-citas")

public interface CitasClient {
	
	
	
	@GetMapping("/id-medico/{idMedico}/citas-asignadas")
	Void medicoTieneCitasAsignadas(@PathVariable Long idMedico);
	
	
	@GetMapping("/id-paciente/{idPaciente}/citas-asignadas")
	Void pacienteTieneCitasAsignadas(@PathVariable Long idPaciente);
	
}