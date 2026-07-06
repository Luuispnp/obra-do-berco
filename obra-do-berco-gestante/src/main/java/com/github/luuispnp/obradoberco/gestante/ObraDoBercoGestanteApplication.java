package com.github.luuispnp.obradoberco.gestante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ObraDoBercoGestanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObraDoBercoGestanteApplication.class, args);
	}

}
