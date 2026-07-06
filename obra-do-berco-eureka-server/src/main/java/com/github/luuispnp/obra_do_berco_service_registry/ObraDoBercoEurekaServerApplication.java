package com.github.luuispnp.obra_do_berco_service_registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ObraDoBercoEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObraDoBercoEurekaServerApplication.class, args);
	}

}
