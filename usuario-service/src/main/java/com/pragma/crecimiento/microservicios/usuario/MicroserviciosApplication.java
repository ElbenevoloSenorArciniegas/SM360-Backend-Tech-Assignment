package com.pragma.crecimiento.microservicios.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MicroserviciosApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosApplication.class, args);
	}
}
