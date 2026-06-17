package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for ProductController.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    /** MockMvc for HTTP request simulation. */
    @Autowired
    private MockMvc mockMvc;

    /** Mocked product service. */
    @MockBean
    private ProductService productService;

    /** ObjectMapper for JSON serialization. */
    @Autowired
    private ObjectMapper objectMapper;

    /** Sample product for testing. */
    private Product sampleProduct;

    /**
     * Set up test data.
     */
    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High-performance laptop")
                .price(15000000.0)
                .stock(10)
                .build();
    }

    /**
     * Test GET all products returns 200 OK.
     */
    @Test
    @DisplayName("GET /api/products should return 200 with all products")
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts())
                .thenReturn(Arrays.asList(sampleProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(15000000.0));
    }

    /**
     * Test GET product by ID returns 200 when found.
     */
    @Test
    @DisplayName("GET /api/products/{id} should return 200 when product exists")
    void testGetProductByIdFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(sampleProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    /**
     * Test GET product by ID returns 404 when not found.
     */
    @Test
    @DisplayName("GET /api/products/{id} should return 404 when product not found")
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test POST create product returns 201 Created.
     */
    @Test
    @DisplayName("POST /api/products should return 201 when product created")
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    /**
     * Test PUT update product returns 200 when found.
     */
    @Test
    @DisplayName("PUT /api/products/{id} should return 200 when product updated")
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(Optional.of(sampleProduct));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    /**
     * Test DELETE product returns 204 when deleted.
     */
    @Test
    @DisplayName("DELETE /api/products/{id} should return 204 when deleted")
    void testDeleteProductSuccess() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Test DELETE product returns 404 when not found.
     */
    @Test
    @DisplayName("DELETE /api/products/{id} should return 404 when not found")
    void testDeleteProductNotFound() throws Exception {
        when(productService.deleteProduct(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound());
    }
}
