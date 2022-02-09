package com.pragma.crecimiento.microservicios.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableEurekaClient
@SpringBootApplication
public class UsuarioApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}
}
