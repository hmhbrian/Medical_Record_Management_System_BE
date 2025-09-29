package com.example.clinicbooking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(title = "ClinicBooking API", version = "v1", description = "Internal Training APIs")
)

@SpringBootApplication
public class ClinicBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicBookingApplication.class, args);
	}

}