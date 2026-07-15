package github.luuispnp.obra_do_berco_eventos;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ObraDoBercoEventosApplication {

	public static void main(String[] args) {
		Dotenv.configure().ignoreIfMissing().load()
				.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(ObraDoBercoEventosApplication.class, args);
	}

}
