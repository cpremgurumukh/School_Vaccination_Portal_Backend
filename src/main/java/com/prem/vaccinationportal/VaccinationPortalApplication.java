package com.prem.vaccinationportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.prem.vaccinationportal")
public class VaccinationPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinationPortalApplication.class, args);
	}

}
