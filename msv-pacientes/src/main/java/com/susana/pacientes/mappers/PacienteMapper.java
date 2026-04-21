package com.susana.pacientes.mappers;

import com.susana.commons.dto.PacienteRequest;
import com.susana.commons.dto.PacienteResponse;
import com.susana.commons.enums.EstadoRegistro;
import com.susana.commons.mappers.CommonMapper;
import com.susana.pacientes.entities.Paciente;
import org.springframework.stereotype.Component;


@Component

public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteResponse, Paciente>{


    @Override
    public Paciente requestAEntidad(PacienteRequest request) {

        if (request == null) return  null;

        return Paciente.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .peso(request.peso())
                .estatura(request.estatura())
                .email(request.email())
                .telefono(request.telefono())
                .direccion(request.direccion())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();


    }

    @Override
    public PacienteResponse entidadAResponse(Paciente entity) {

        if (entity == null) return null;


        return  new PacienteResponse(
                entity.getId(),
                String.join("",
                		entity.getNombre(),
                        entity.getApellidoPaterno(),
                        entity.getApellidoMaterno()
                ),
                entity.getEdad(),
                entity.getPeso(),
                entity.getEstatura(),
                entity.getImc(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getDireccion(),
                entity.getNumExpediente()
        );

    }
}

