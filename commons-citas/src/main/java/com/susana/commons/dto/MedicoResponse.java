package com.susana.commons.dto;

public record MedicoResponse(
        Long id,
        String nombre,
        Integer edad,
        String email,
        String telefono,
        String cedulaProfesional,
        String especialidad,
        String disponibilidad
) {
}
