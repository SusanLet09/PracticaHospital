package com.susana.medicos.services;



import com.susana.commons.clients.CitasClient;
import com.susana.commons.clients.MedicoClient;
import com.susana.commons.dto.MedicoRequest;
import com.susana.commons.dto.MedicoResponse;
import com.susana.commons.enums.DisponibilidadMedico;
import com.susana.commons.enums.EspecialidadMedico;
import com.susana.commons.enums.EstadoRegistro;
import com.susana.commons.exceptions.RecursoNoEncontradoExceptions;
import com.susana.medicos.entities.Medico;

import com.susana.medicos.mappers.MedicoMapper;
import com.susana.medicos.repositories.MedicoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j

public class MedicoServiceImpl implements MedicoService{

	private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final CitasClient citasClient;
    private final MedicoClient medicoClient;

    @Override
    public List<MedicoResponse> listar() {
            return estadoMedico(EstadoRegistro.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true )
    public MedicoResponse obtenerPorId(Long id) {
    	//validarEstadoPorId(EstadoRegistro.ACTIVO, id);
        Medico medico = obtenerMedicoActivoOException(id);
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
    	log.info("Buscando el médico con id: {}", id);
		 return medicoMapper.entidadAResponse(medicoRepository.findById(id).orElseThrow());
    }
 

    @Override
    public MedicoResponse registrar(MedicoRequest request) {

        validarEmailPorEstado(request.email(), EstadoRegistro.ACTIVO);
        validarTelefonoPorEstado(request.telefono(), EstadoRegistro.ACTIVO);
        validarCedulaUnica(request.cedulaProfesional(),EstadoRegistro.ACTIVO);
        
        Medico medico = medicoMapper.requestAEntidad(request);
        
        medico.setIdEspecialidad(
        		EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad())
        		);
        
        medico.setDisponibilidad(DisponibilidadMedico.DISPONIBLE);
        
        medicoRepository.save(medico);
        
        log.info("Nuevo Medico {} registrado" , medico.getNombre());
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoActivoOException(id);
        
        log.info("Actualizando medico con id: {} ", id);
        
        medicoTieneCitasAsignadas(id);
        

        validarActualizarUnicos(request, EstadoRegistro.ACTIVO,id);
        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));
        
        log.info("Medico actualizado con exito {}", id);

        return medicoMapper.entidadAResponse(medico);

    }
    @Override
    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
    	Medico medico = obtenerMedicoActivoOException(idMedico);
    	
    	DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico
    			.obtenerDisponibilidadPorCodigo(idDisponibilidad);
    	
    	if( medico.getDisponibilidad() == nuevaDisponibilidad)
    		return;
    	
    	DisponibilidadMedico anteriorDisponibilidad = medico.getDisponibilidad();
    	
    	medico.setDisponibilidad(nuevaDisponibilidad);
    	
    	log.info("Disponibilidad del medico con id {} cambio de {} a {}",
    			
    			idMedico, anteriorDisponibilidad, nuevaDisponibilidad);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoActivoOException(id);
        log.info("Eliminando Medico con id: {}", id);
        
        medicoTieneCitasAsignadas(id);
        
  
   
        log.info("Medico con id: {} ha sido ELIMINADO");

        
    }

    private Medico obtenerMedicoActivoOException(Long id){
    	
        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(()-> 
        new RecursoNoEncontradoExceptions("Medico no encontrado con el id "+ id));
    }

    private List<MedicoResponse> estadoMedico(EstadoRegistro estados) {
        return medicoRepository.findByEstadoRegistro(estados)
                .stream()
                .map(medicoMapper::entidadAResponse)
                .toList();
    }

  /*  private void validarEstadoPorId(EstadoRegistro estados, Long id) {
        log.info("validando estado por id...");
        if (!medicoRepository.existsByEstadoRegistroAndId(estados, id))
            throw new IllegalArgumentException("El medico no se encuetra activo con el id: " + id);
    }*/

    private void validarEmailPorEstado(String email, EstadoRegistro estados) {
        log.info("validando email por estado...");
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(email, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico con el emial: " + email);
    }
    
    private void validarCedulaUnica(String cedulaProfesional, EstadoRegistro estados) {
        log.info("validando email por estado...");
        if (medicoRepository.existsByCedulaProfesionalAndEstadoRegistro(cedulaProfesional, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico con la cedula profesional: " + cedulaProfesional);
    }

    private void validarTelefonoPorEstado(String telefono, EstadoRegistro estados) {
        log.info("validando email por estado...");
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un medico con el telefono: " + telefono);
    }

    private void validarActualizarUnicos(MedicoRequest request, EstadoRegistro estados, Long id) {
        log.info("validando actualizacion por estado...");
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un medico con el emial: " + request.email());

        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un medico con el telefono: " + request.telefono());
    
        if (medicoRepository.existsByCedulaProfesionalAndIdNotAndEstadoRegistro(request.cedulaProfesional(),id , EstadoRegistro.ACTIVO))
        	throw new IllegalArgumentException("Ya existe un medico con la cedula profesional: " + request.telefono());
        
    }
    private void medicoTieneCitasAsignadas(Long id) {
    	citasClient.medicoTieneCitasAsignadas(id);
    }
}
