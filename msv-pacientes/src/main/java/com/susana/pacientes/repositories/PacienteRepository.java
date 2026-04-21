package com.susana.pacientes.repositories;

import com.susana.pacientes.entities.Paciente;
import com.susana.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	
	
List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistrp);
	
	Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
    boolean existsByEmailIgnoreCaseAndEstadoRegistro(
            String email,
            EstadoRegistro estadoRegistro
    );

    boolean existsByTelefonoIgnoreCaseAndEstadoRegistro(
            String telefono,
            EstadoRegistro estadoRegistro
    );

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
            String email,
            EstadoRegistro estadoRegistro,
            Long id
    );

    boolean existsByTelefonoIgnoreCaseAndEstadoRegistroAndIdNot(
            String telefono,
            EstadoRegistro estadoRegistro,
            Long id
    );
    
}
