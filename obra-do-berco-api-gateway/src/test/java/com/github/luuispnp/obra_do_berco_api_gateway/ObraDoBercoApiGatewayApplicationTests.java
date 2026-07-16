package com.github.luuispnp.obra_do_berco_api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "eureka.client.enabled=false")
class ObraDoBercoApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}
