package com.pragma.crecimiento.microservicios.imagen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ImagenApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ImagenApplication.class, args);
	}
}
