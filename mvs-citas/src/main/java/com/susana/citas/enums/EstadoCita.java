package com.susana.citas.enums;

import java.util.EnumSet;
import java.util.Set;

import com.susana.commons.exceptions.RecursoNoEncontradoExceptions;
import com.susana.commons.utils.StringCustomUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter


public enum EstadoCita {
	
	PENDIENTE(1L, "Pendiente de confirmar") {
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(CONFIRMADA, CANCELADA);
		}
	},
	
    CONFIRMADA(2L, "Confirmada por el paciente") {
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(EN_CURSO, CANCELADA);
		}
	},
    
    EN_CURSO(3L, "Paciente llegó a su cita") {
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return EnumSet.of(FINALIZADA);
		}
	},
    
    FINALIZADA(4L, "Cita finalizada") {
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return Set.of();
		}
	},
    
	CANCELADA(5L, "Cita cancelada") {
		@Override
		public Set<EstadoCita> puedeCambiar() {
			return Set.of();
		}
	};
    
    private final Long codigo;
	private final String descripcion;
	
	public abstract Set<EstadoCita> puedeCambiar();
	
	public static EstadoCita obtenerEstadoCitaPorCodigo(Long codigo) {
        for (EstadoCita e : values()) {
            if (e.codigo == codigo) {
                return e;
            }
        }
        throw new RecursoNoEncontradoExceptions("Código de cita no válido: " + codigo);
    }
	
	public static EstadoCita obtenerEstadoCitaPorDescripcion(String descripcion) {
        for (EstadoCita e : values()) {
        	String descEstadoCita= StringCustomUtils.quitarAcentos(e.descripcion);
            if (descEstadoCita.equalsIgnoreCase(StringCustomUtils.quitarAcentos(descripcion))) {
                return e;
            }
        }
        throw new RecursoNoEncontradoExceptions("Descripción de cita no válida: " + descripcion);
    }
}
