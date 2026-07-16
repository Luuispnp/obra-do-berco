package com.github.luuispnp.obra_do_berco_document;

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
	void fichaDownloadRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/documentos/ficha/00000000-0000-0000-0000-000000000000"))
				.andExpect(status().isUnauthorized());
	}

}
