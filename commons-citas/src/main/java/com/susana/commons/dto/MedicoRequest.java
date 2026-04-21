package com.susana.commons.dto;

import jakarta.validation.constraints.*;

public record MedicoRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 3, max = 50, message = "El nombre debe ser amyor a 3 y menos a 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 3, max = 50, message = "El apellido paterno debe ser amyor a 3 y menos a 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 3, max = 50, message = "El apellido materno debe ser amyor a 3 y menos a 50 caracteres")
        String apellidoMaterno,

        @NotNull(message = "La edad es requerida")
        @Positive(message = "La edad debe ser positiva")
        @Max(value = 100)
        @Min(value = 18, message = "La edad debe ser mayor o igual a 18")
        Integer edad,

        @NotBlank(message = "El correo electronico es requerido")
        @Size(max = 100)
        @Email(message = "El email debe de tener un formato valido (ejemplo@gmail.com)")
        String email,

        @NotBlank(message = "El numero de telefono es requerido")
        @Size(max = 10, message = "El numero de telefono debe tener 10 digitos y deben ser de 0 al 9")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
        String telefono,

        @NotBlank(message = "La cedula es requerida")
        @Size(max = 12, message = "La cedula debe contener 12 caracteres")
        String cedulaProfesional,

        @NotNull(message = "La especialidad requerido")
        @Max(value = 12, message = "La especialidad debe ser correspondienrte ")
        Long idEspecialidad

) {
}
