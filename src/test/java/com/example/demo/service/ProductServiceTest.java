package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for ProductService.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    /** Mocked product repository. */
    @Mock
    private ProductRepository productRepository;

    /** Product service under test. */
    @InjectMocks
    private ProductService productService;

    /** Sample product for testing. */
    private Product sampleProduct;

    /**
     * Set up test data before each test.
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
     * Test get all products returns list.
     */
    @Test
    @DisplayName("Should return all products")
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(sampleProduct,
                Product.builder().id(2L).name("Mouse").price(200000.0).stock(50).build());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    /**
     * Test get product by ID when found.
     */
    @Test
    @DisplayName("Should return product when found by ID")
    void testGetProductByIdFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
    }

    /**
     * Test get product by ID when not found.
     */
    @Test
    @DisplayName("Should return empty when product not found by ID")
    void testGetProductByIdNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(99L);

        assertFalse(result.isPresent());
    }

    /**
     * Test create product successfully.
     */
    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        Product result = productService.createProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    /**
     * Test update product when found.
     */
    @Test
    @DisplayName("Should update product when found")
    void testUpdateProductFound() {
        Product updated = Product.builder()
                .name("Gaming Laptop")
                .description("Updated description")
                .price(20000000.0)
                .stock(5)
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        Optional<Product> result = productService.updateProduct(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("Gaming Laptop", result.get().getName());
    }

    /**
     * Test delete product when it exists.
     */
    @Test
    @DisplayName("Should delete product when it exists")
    void testDeleteProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    /**
     * Test delete product when it does not exist.
     */
    @Test
    @DisplayName("Should return false when deleting non-existent product")
    void testDeleteProductNotExists() {
        when(productRepository.existsById(99L)).thenReturn(false);

        boolean result = productService.deleteProduct(99L);

        assertFalse(result);
        verify(productRepository, times(0)).deleteById(any());
    }

    /**
     * Test search products by name.
     */
    @Test
    @DisplayName("Should search products by name")
    void testSearchByName() {
        when(productRepository.findByNameContainingIgnoreCase("laptop"))
                .thenReturn(List.of(sampleProduct));

        List<Product> result = productService.searchByName("laptop");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }
}
