package com.susana.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.susana.pacientes","com.susana.commons"})
public class MsvPacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvPacientesApplication.class, args);
	}

}
