package com.susana.medicos.repositories;


import com.susana.commons.enums.EstadoRegistro;
import com.susana.medicos.entities.Medico;
import com.susana.medicos.repositories.MedicoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface MedicoRepository extends JpaRepository<Medico, Long> {
	
	Optional<Medico>findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

	List<Medico> findByEstadoRegistro(EstadoRegistro estados);

    boolean existsByEstadoRegistroAndId(EstadoRegistro estados, Long id);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String emial, EstadoRegistro estados);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estados);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(String email, EstadoRegistro estados, Long id);

    boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estados, Long id);

    boolean existsByCedulaProfesionalAndEstadoRegistro(String cedulaProfesional, EstadoRegistro estado);

    boolean existsByCedulaProfesionalAndIdNotAndEstadoRegistro(String cedulaProfesional,Long id ,EstadoRegistro estado);
    


}
