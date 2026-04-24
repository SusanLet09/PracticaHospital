package com.susana.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.susana.citas", "com.susana.commons"})
public class MvsCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvsCitasApplication.class, args);
	}

}
