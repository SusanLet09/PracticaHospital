package com.susana.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import com.susana.commons.dto.PacienteResponse;


@FeignClient (name = "msv-pacientes")

public interface PacienteClient {
	
	@GetMapping("/{id}")
    PacienteResponse obtenerPacienteActivoPorId(@PathVariable Long id);

	@GetMapping("/id-paciente/{id}")
	PacienteResponse obtenerPacientePorIdSinEstado(@PathVariable Long id);

}
