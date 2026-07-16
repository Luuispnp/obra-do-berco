package com.github.luuispnp.obradoberco.gestante;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = "eureka.client.enabled=false")
@AutoConfigureMockMvc
class SmokeTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void listGestantesRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/gestantes"))
				.andExpect(status().isUnauthorized());
	}

}
