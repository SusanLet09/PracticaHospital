package com.susana.pacientes.services;


import com.susana.commons.clients.CitasClient;
import com.susana.commons.dto.PacienteRequest;
import com.susana.commons.dto.PacienteResponse;
import com.susana.pacientes.entities.Paciente;
import com.susana.commons.enums.EstadoRegistro;
import com.susana.commons.exceptions.RecursoNoEncontradoExceptions;
import com.susana.pacientes.mappers.PacienteMapper;
import com.susana.pacientes.repositories.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j

public class PacienteServiceImpl implements PacienteService{

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;
    
    private final CitasClient citasClient;


    @Override
    @Transactional
    
    public List<PacienteResponse> listar() {
    	
		log.info("Listado de todas las citas activas solicitadas");
    	
    	
        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
   
    }
    
    

    @Override
    public PacienteResponse obtenerPorId(Long id) {
    	return pacienteMapper.entidadAResponse(obtenerPacienteActivo0Exception(id)); 
       
    }   
    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientePorIdSinEstado(Long id) {
    	log.info("Buscando paciente sin estado con id: {}", id);
    	
    	return pacienteMapper.entidadAResponse(pacienteRepository.findById(id).orElseThrow(() -> 
    	new RecursoNoEncontradoExceptions("Paciente con id: {} no encontrado" + id)));
    	
    }
    
    


    @Override
    public PacienteResponse registrar(PacienteRequest request) {

        log.info("Registrando nuevo paciente...");

        validarEmailUnico(request.email());
        validarTelefonoUnico(request.telefono());

        Paciente paciente = pacienteMapper.requestAEntidad(request);
        
        paciente.setImc(paciente.calcularImc());
        paciente.setNumExpediente(paciente.generarNumExpediente());
        
        log.info("Telefono: {}", paciente.getTelefono());
        log.info("Expediente generado: {}", paciente.getNumExpediente());
        
        pacienteRepository.save(paciente);
        
        log.info("Paciente registrado con esto: {}", paciente.getNombre());
        return pacienteMapper.entidadAResponse(paciente);
        
    }
    
    
    

    @Override
    
    public PacienteResponse actualizar(PacienteRequest request, Long id) {

    	Paciente paciente = obtenerPacienteActivo0Exception(id);
   	 
   	 pacienteTieneCitasAsignadas(id);
   	
   	log.info("Actualizando paciente : {} ", paciente.getNombre());
   	

       validarCambiosUnicos(request, id);

       paciente.actualizar(
               request.nombre(),
               request.apellidoPaterno(),
               request.apellidoMaterno(),
               request.edad(),
               request.peso(),
               request.estatura(),
               request.email(),
               request.telefono(),
               request.direccion()
               
       );

       paciente.setImc(paciente.calcularImc());
       paciente.setNumExpediente(paciente.generarNumExpediente());


       log.info("Paciente actualizado con éxito: {}", paciente.getNombre());

       return pacienteMapper.entidadAResponse(paciente);
    }

    
    
    

    @Override
    public void eliminar(Long id) {

        Paciente paciente = obtenerPacienteActivo0Exception(id);
        
        log.info("Eliminando Paciente con id: {} ", id);
        
        pacienteTieneCitasAsignadas(id);

 
        paciente.setEstadoRegistro(EstadoRegistro.ELIMINADO);
       
        log.info("El paciente con el id {} eliminado", id);
        
        



    }
  /*  private String generarExpediente(String telefono){
        String ultimos4 = telefono.substring(telefono.length() - 4);
        long tiempo = System.currentTimeMillis() % 1000000;

        return "PAC" + ultimos4 + tiempo;


    }*/
    private Paciente obtenerPacienteActivo0Exception (Long id){
        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(()-> new RecursoNoEncontradoExceptions("Paciente no encontrado"));
    }
    
    private void validarEmailUnico(String email) {

        log.info("Validando email único...");

        if (pacienteRepository
                .existsByEmailIgnoreCaseAndEstadoRegistro(
                        email,
                        EstadoRegistro.ACTIVO
                )) {

            throw new IllegalArgumentException(
                    "Ya existe un paciente registrado con el email: " + email
            );
        }
    }

    private void validarTelefonoUnico(String telefono) {

        log.info("Validando teléfono único...");

        if (pacienteRepository
                .existsByTelefonoIgnoreCaseAndEstadoRegistro(
                        telefono,
                        EstadoRegistro.ACTIVO
                )) {

            throw new IllegalArgumentException(
                    "Ya existe un paciente registrado con el teléfono: " + telefono
            );
        }
    }

    private void validarCambiosUnicos(PacienteRequest request, Long id) {

        log.info("Validando email al actualizar...");

        if (pacienteRepository
                .existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                        request.email(),
                        EstadoRegistro.ACTIVO,
                        id
                )) {

            throw new IllegalArgumentException(
                    "Ya existe un paciente registrado con el email: "
                            + request.email()
            );
        }

        log.info("Validando teléfono al actualizar...");

        if (pacienteRepository
                .existsByTelefonoIgnoreCaseAndEstadoRegistroAndIdNot(
                        request.telefono(),
                        EstadoRegistro.ACTIVO,
                        id
                )) {

            throw new IllegalArgumentException(
                    "Ya existe un paciente registrado con el teléfono: "
                            + request.telefono()
            );
        }
    }
    private void pacienteTieneCitasAsignadas(Long id) {
    	citasClient.pacienteTieneCitasAsignadas(id);
    }
  
    
}
