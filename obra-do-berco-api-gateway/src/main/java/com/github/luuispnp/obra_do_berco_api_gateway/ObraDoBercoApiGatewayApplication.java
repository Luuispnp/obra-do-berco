package com.github.luuispnp.obra_do_berco_api_gateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ObraDoBercoApiGatewayApplication {

	public static void main(String[] args) {
		Dotenv.configure().ignoreIfMissing().load()
				.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ObraDoBercoApiGatewayApplication.class, args);
	}

}
