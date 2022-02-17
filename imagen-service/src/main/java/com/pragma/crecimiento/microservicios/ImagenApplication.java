package com.pragma.crecimiento.microservicios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableEurekaClient
@SpringBootApplication
public class ImagenApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ImagenApplication.class, args);
	}
}
