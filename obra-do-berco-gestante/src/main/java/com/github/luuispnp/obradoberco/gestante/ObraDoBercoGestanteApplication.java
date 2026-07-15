package com.github.luuispnp.obradoberco.gestante;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ObraDoBercoGestanteApplication {

	public static void main(String[] args) {
		Dotenv.configure().ignoreIfMissing().load()
				.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ObraDoBercoGestanteApplication.class, args);
	}

}
