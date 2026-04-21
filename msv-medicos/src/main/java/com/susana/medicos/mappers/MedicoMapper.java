package com.susana.medicos.mappers;


import com.susana.commons.dto.MedicoRequest;
import com.susana.commons.dto.MedicoResponse;
import com.susana.commons.enums.EstadoRegistro;
import com.susana.commons.mappers.CommonMapper;
import com.susana.medicos.entities.Medico;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse,Medico>{


    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        if (request == null) return null;

        return Medico.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .email(request.email())
                .telefono(request.telefono())
                .cedulaProfesional(request.cedulaProfesional())
                .cedulaProfesional(request.cedulaProfesional())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        if (entidad == null) return null;

        return new MedicoResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedulaProfesional(),
                entidad.getIdEspecialidad().getDescripcion(),
                entidad.getDisponibilidad().getDescripcion());
    }
}
