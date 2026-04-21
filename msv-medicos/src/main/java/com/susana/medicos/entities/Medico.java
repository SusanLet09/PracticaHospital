package com.susana.medicos.entities;


import com.susana.commons.enums.DisponibilidadMedico;
import com.susana.commons.enums.EspecialidadMedico;
import com.susana.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    Integer edad;

    @Column(name = "EMAIL", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "TELEFONO", unique = true, nullable = false, length = 10)
    private String telefono;

    @Column(name = "CEDULA_PROFESIONAL", unique = true, nullable = false, length = 12)
    String cedulaProfesional;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESPECIALIDAD", unique = true, nullable = false)
    private EspecialidadMedico idEspecialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDAD", unique = true, nullable = false)
    private DisponibilidadMedico disponibilidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", unique = true, nullable = false, length = 10)
    private EstadoRegistro estadoRegistro;


    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Integer edad, String email, String telefono, String cedulaProfesional, EspecialidadMedico idEspecialidad) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.email = email;
        this.telefono = telefono;
        this.cedulaProfesional = cedulaProfesional;
        this.idEspecialidad = idEspecialidad;
    }
}
