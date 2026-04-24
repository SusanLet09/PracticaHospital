package com.susana.citas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.susana.citas.entities.Cita;
import com.susana.citas.enums.EstadoCita;
import com.susana.commons.enums.EstadoRegistro;



@Repository

public interface CitaRepository extends JpaRepository<Cita, Long>{
	
	
	List <Cita>findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	/*boolean existsByIdMedicoAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);*/
	
	boolean existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(Long idMedico, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);
	
	/*boolean existsByIdPacienteAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);*/

	boolean existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(Long idPaciente, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);

}





 

