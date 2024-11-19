package com.ecart.product_service;

import com.ecart.product_service.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

//	@Container
//	static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:5.7.34"));

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		//dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.url", () -> "jdbc:mysql://localhost:3306/ecart_product_service");
		dynamicPropertyRegistry.add("spring.datasource.username", () -> "root");
		dynamicPropertyRegistry.add("spring.datasource.password", () -> "root");
	}

	@Test
	void shouldCreateProduct() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getProductRequest())))
				.andExpect(status().isCreated());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.price(35000)
				.name("Iphone")
				.description("new model")
				.build();
	}

	@Test
	void contextLoads() {
	}

}
