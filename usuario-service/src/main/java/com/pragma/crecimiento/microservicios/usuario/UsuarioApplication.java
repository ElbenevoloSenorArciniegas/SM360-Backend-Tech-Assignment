package com.pragma.crecimiento.microservicios.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class UsuarioApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}
}
