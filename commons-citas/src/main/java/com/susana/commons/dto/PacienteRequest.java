package com.susana.commons.dto;

import jakarta.validation.constraints.*;



public record PacienteRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El apellido paterno debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @NotNull(message = "La edad  es requerido")
        @Positive(message = "La edad debe de ser positiva")
        @Max(value = 100, message = "La edad debe tener entre 1 y 100 caracteres")
        Short edad,


        @NotNull(message = "El peso es obligatorio")
        @DecimalMin(value = "0.1", message = "El peso debe ser mayor a 0")
        @DecimalMax(value = "200.0", message = "El peso máximo es 200 kg")
        Double peso,

        @NotNull(message = "La estatura es obligatoria")
        @DecimalMin(value = "1.0", message = "La estatura mínima es 1.0 m")
        @DecimalMax(value = "2.0", message = "La estatura máxima es 2.0 m")
        @Positive(message = "La estatura debe de ser positiva")
        Double estatura,

        @NotBlank(message = "El email es obligatorio")
        @Size(max= 100, message = "El email no puede exceder 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos numéricos")
        String telefono,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(min = 1, max = 150, message = "La dirección debe tener entre 1 y 150 caracteres")
        String direccion


) {
}
