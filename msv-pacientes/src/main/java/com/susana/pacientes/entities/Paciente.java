package com.susana.pacientes.entities;


import com.susana.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Service;


@Service
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "pacientes")

public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "edad", nullable = false)
    private Short edad;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "estatura", nullable = false)
    private Double estatura;

    @Column(name = "imc", nullable = false)
    private Double imc;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "num_expediente", nullable = false, length = 20)
    private String numExpediente;

   
    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "estado_registro", nullable = false)
    private EstadoRegistro estadoRegistro;


    public  void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, Double peso, Double estatura, 
    		String email, String telefono, String direccion) {

        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.imc = this.calcularImc();
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.numExpediente = this.generarNumExpediente();
        
    }
    
    public double calcularImc() {
    	return this.peso / (this.estatura * this.estatura );
    }
    
    public String generarNumExpediente() {
    	
    	StringBuilder expediente = new StringBuilder();
    	
    	for (char c : this.telefono.toCharArray())
    		expediente.append(c).append("X");
    	
    	return expediente.toString();
    	
    }

    }



