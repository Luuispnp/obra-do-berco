package com.github.luuispnp.obra_do_berco_solicitacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ObraDoBercoSolicitacoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObraDoBercoSolicitacoesApplication.class, args);
	}

}
