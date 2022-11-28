package com.ducnt.productservice;

import com.ducnt.productservice.dtos.ProductRequest;
import com.ducnt.productservice.models.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductServiceApplicationTests extends AbstractIntegrationTest {

    @Test
    void shouldCreateProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getProductRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.name", Matchers.is("Test product")))
                .andExpect(jsonPath("$.description", Matchers.is("Some description")))
                .andExpect(jsonPath("$.price", Matchers.is(1400)));
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void shouldGetAllProduct() throws Exception {
        productRepository.saveAll(List.of(
                Product.builder()
                        .name("Test new product")
                        .description("Description")
                        .price(BigDecimal.TEN)
                        .build(),
                Product.builder()
                        .name("Test new product")
                        .description("Description")
                        .price(BigDecimal.TEN)
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("size()", Matchers.is(2)));
        Assertions.assertEquals(2, productRepository.findAll().size());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
                .name("Test product")
                .description("Some description")
                .price(BigDecimal.valueOf(1400))
                .build();
    }

}
