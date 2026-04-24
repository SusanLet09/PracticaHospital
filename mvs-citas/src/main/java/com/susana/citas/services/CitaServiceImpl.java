package com.susana.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.susana.citas.dto.CitaResponse;
import com.susana.citas.entities.Cita;
import com.susana.citas.enums.EstadoCita;
import com.susana.citas.dto.CitaRequest;
import com.susana.citas.mappers.CitaMapper;
import com.susana.citas.repositories.CitaRepository;
import com.susana.commons.clients.MedicoClient;
import com.susana.commons.clients.PacienteClient;
import com.susana.commons.dto.MedicoResponse;
import com.susana.commons.dto.PacienteResponse;
import com.susana.commons.enums.DisponibilidadMedico;
import com.susana.commons.enums.EstadoRegistro;
import com.susana.commons.exceptions.EntidadRelacionadaException;
import com.susana.commons.exceptions.RecursoNoEncontradoExceptions;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Slf4j
@Transactional

public class CitaServiceImpl implements CitaService{


	
	private final CitaRepository citaRepository;
	
	private final CitaMapper citaMapper;
	
	private final MedicoClient medicoClient;
	
	private final PacienteClient pacienteClient;
	
	//private final List<EstadoCita> ESTADOS_INVALIDOS_MODIFICACION = List.of(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);
	
	
	
	private final List<EstadoCita> ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS = List.of( EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);


	@Override
    @Transactional
	public List<CitaResponse> listar() {
		
		log.info("Listado de todas las citas activas solicitado");
		
	
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
        		
                .map(cita -> 
                	citaMapper.entidadAResponse(
                			cita, 
							obtenerPacienteSinEstado(cita.getIdPaciente()),
                			obtenerMedicoSinEstado(cita.getIdMedico()))
                	).toList();
                	
	}        
	

	@Override
	
	public CitaResponse obtenerPorId(Long id) {
		
		Cita cita = obtenerCitaActivaOException(id);
		return citaMapper.entidadAResponse(
				cita,
				obtenerPacienteSinEstado(cita.getIdPaciente()), 
				obtenerMedicoSinEstado(cita.getIdMedico()));
		
	
	                
	}
	
	
	@Override
	public void medicoTieneCitasAsignadas(Long IdMedico) {
		
		log.info("Buscando citas asignadas con estados: {} o {} para el medico con id: {} ", 
				ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS, IdMedico);
		
		
		boolean tieneCitas = citaRepository
				.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(IdMedico, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		
		if(tieneCitas)
			throw new EntidadRelacionadaException("No se puede modificar el medico ya que tiene citas con estados: " + ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		
	}
	
	
	@Override
	public void pacienteTieneCitasAsignadas(Long IdPaciente) {
		
		log.info("Buscando citas asignadas con estados: {} o {} para el paciente con id: {} ", 
				ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS, IdPaciente);
		
		
		boolean tieneCitas = citaRepository
				.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(IdPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		
		if(tieneCitas)
			throw new EntidadRelacionadaException("No se puede modificar el medico ya que tiene citas con estados: " + ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		

	}
	


	@Override
	public CitaResponse registrar(CitaRequest request) {
		
		log.info("Registrando nueva Cita: {}", request);
		
		MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
		
		validarDisponibilidadMedico(medico);
		
		PacienteResponse paciente = obtenerPacienteActivo(request.idPaciente());
		
		validarPacienteTieneRegistrosAsignados(request.idPaciente());
		
		validarMedicoTieneRegistrosAsignados(request.idMedico());
		
		Cita cita = citaRepository.save(citaMapper.requestAEntidad(request));
		
		cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
		
		log.info("Cita registrada exitosamente: {}", cita.getId());
		
		return citaMapper.entidadAResponse(cita, paciente, medico);
		

				
	}
		 

	@Override
	public CitaResponse actualizar(CitaRequest request, Long id) {
		
		Cita cita = obtenerCitaActivaOException(id); 
		
		log.info("Actualizando Cita con id: {}", cita.getId());
		
		Long idMedicoAnterior = cita.getIdMedico();
		
		EstadoCita nuevoEstadoCita = request.idEstadoCita() != null ?
				EstadoCita.obtenerEstadoCitaPorCodigo(request.idEstadoCita()) : null;
		
		if (nuevoEstadoCita == EstadoCita.CANCELADA) {
			cita.actualizarEstadoCita(nuevoEstadoCita);
			
			cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
			
			return citaMapper.entidadAResponse(cita,
					obtenerPacienteActivo(cita.getIdPaciente()),
					obtenerMedicoActivo(cita.getIdMedico()));
		}
		
		MedicoResponse nuevoMedico = cita.getIdMedico().equals(request.idMedico()) ?
				null : obtenerMedicoActivo(request.idMedico());
		
		PacienteResponse nuevoPaciente = cita.getIdPaciente().equals(request.idPaciente()) ?
				null : obtenerPacienteActivo(request.idPaciente());
		
		if(nuevoMedico != null) {
			validarDisponibilidadMedico(nuevoMedico);
		}
		
		if(nuevoPaciente != null) {
			validarPacienteTieneRegistrosAsignados(request.idPaciente());
		}
		
		cita.actualizar(
				request.idPaciente(),
				request.idMedico(),
				request.fechaCita(),
				request.sintomas());
		
		if (nuevoEstadoCita == EstadoCita.CONFIRMADA) {
			cita.actualizarEstadoCita(nuevoEstadoCita);
		}
		
		if (nuevoMedico != null) {
			
			cambiarDisponibilidadMedico(
					idMedicoAnterior,
					DisponibilidadMedico.DISPONIBLE.getCodigo());
		}
		
		cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
		
		return citaMapper.entidadAResponse(
				cita,
				obtenerPacienteActivo(cita.getIdPaciente()),
				obtenerMedicoActivo(cita.getIdMedico()));
	}  
	

	@Override
	public void eliminar(Long id) {
		
		Cita cita = obtenerCitaActivaOException(id);
		
		
		log.info("Eliminando cita con id: {}", id);
		
		
		cita.eliminar();
		
		if(cita.getEstadoCita() == EstadoCita.PENDIENTE || cita.getEstadoCita() == EstadoCita.CONFIRMADA)

		
		log.info("Cita con id {} ha sido marcada como eliminada", id);
		
		
	
	}


	 @Override
	 public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
		 
		 Cita cita = obtenerCitaActivaOException(idCita);
			log.info("Actualizando estado de la cita: {}", cita.getId());
			
			EstadoCita estadoCita = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);
			cita.actualizarEstadoCita(estadoCita);
			
			cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
			
			log.info("Estado de la cita {} actualizado correctamente", cita.getId());
			
		}
	 
	 private Cita obtenerCitaActivaOException(Long id) {
		 log.info("Buscando Cita activa con id: {}", id);
			return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
					() -> new RecursoNoEncontradoExceptions("Cita activa no encontrada con el id: " + id));
		}

	
	 private MedicoResponse obtenerMedicoActivo(Long IdMedico) {
		log.info("Buscando Medico Activo con id {} en el servicio remoto...", IdMedico);
		 return medicoClient.obtenerMedicoActivoPorId(IdMedico);
		 
		 
	 }
	 private MedicoResponse obtenerMedicoSinEstado(Long IdMedico) {
		 log.info("Buscando médico sin estado con id {} en el servicio remoto...", IdMedico);
			return medicoClient.obtenerMedicoPorIdSinEstado(IdMedico);
	 
	 }
	 private PacienteResponse obtenerPacienteActivo(Long IdPaciente) {
		 
		 log.info("Buscando médico activo con id {} en el servicio remoto...", IdPaciente);
			return pacienteClient.obtenerPacienteActivoPorId(IdPaciente);
			
		}
		
		private PacienteResponse obtenerPacienteSinEstado(Long IdPaciente) {
			log.info("Buscando médico sin estado con id {} en el servicio remoto...", IdPaciente);
			return pacienteClient.obtenerPacientePorIdSinEstado(IdPaciente);
		}
		
		
		
		private void validarPacienteTieneRegistrosAsignados(Long IdPaciente) {
			log.info("Validando si el paciente tiene una cita activa con los estados: {}", ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
			
			if( citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
					IdPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS))
				
				throw new EntidadRelacionadaException(
						"No se puede regitrar la cita ya que el paciente solo puede tener una cita activa con los estados: "
								+ ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		}
		
		
		private void validarMedicoTieneRegistrosAsignados(Long IdMedico) {
			
			log.info("Validando si el médico tiene una cita activa con los estados: {}", ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
			
			if( citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
					IdMedico, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS))
				
				throw new EntidadRelacionadaException(
						"No se puede regitrar la cita ya que el médico solo puede tener una cita activa con los estados: "
								+ ESTADOS_INVALIDOS_REGISTRO_ASIGNADOS);
		}
		
		
		
	
		
		private void validarDisponibilidadMedico(MedicoResponse medico) {
			
			log.info("Validando si el médico se encuentra en estado: {}", DisponibilidadMedico.DISPONIBLE);
			
			if(!DisponibilidadMedico.DISPONIBLE.getDescripcion().equalsIgnoreCase(medico.disponibilidad()))
				throw new IllegalStateException("El médico no se encuentra en estado " + DisponibilidadMedico.DISPONIBLE);
		    }
		    
		
		private void cambiarDisponibilidadSegunEstadoCita(Long idMedico, EstadoCita estadoCita) {
			switch(estadoCita) {
			
			case PENDIENTE, CONFIRMADA -> 
			
			cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
			
			case EN_CURSO ->
			
			cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.EN_CONSULTA.getCodigo());
			
			case FINALIZADA, CANCELADA -> {
				
				cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());
				
			    /*boolean tieneOtrasCitas = citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
			        idMedico, EstadoRegistro.ACTIVO, List.of(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO));
			    if (!tieneOtrasCitas)
			        cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());*/
			}
		}
			
		}
		private void cambiarDisponibilidadMedico (Long idMedico, Long idDisponibilidad) {
			
			
			log.info("Actualizando disponibilidad del medico con id {} a {}",
					idMedico, DisponibilidadMedico.obtenerDisponibilidadPorCodigo(idDisponibilidad));
			
			medicoClient.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);
		}
		

}
