package com.susana.citas.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.susana.commons.dto.DatosMedico;
import com.susana.commons.dto.DatosPaciente;

public record CitaResponse(
		
		Long id,
		DatosPaciente paciente,
		DatosMedico medico,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
		LocalDateTime fechaCita,
		String sintomas,
		String estadoCita
		
) {}