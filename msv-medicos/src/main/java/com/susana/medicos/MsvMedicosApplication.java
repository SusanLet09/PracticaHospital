package com.susana.medicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.susana.medicos", "com.susana.commons"})

public class MsvMedicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvMedicosApplication.class, args);
	}

}
