package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Product endpoints.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /** Product service. */
    private final ProductService productService;

    /**
     * Constructor injection.
     *
     * @param productService the product service
     */
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products.
     *
     * @return list of all products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Get product by ID.
     *
     * @param id the product ID
     * @return the product or 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable final Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product with 201 status
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody final Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(product));
    }

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param product the updated product data
     * @return the updated product or 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable final Long id,
            @Valid @RequestBody final Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a product.
     *
     * @param id the product ID
     * @return 204 No Content or 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Search products by name.
     *
     * @param name the name to search
     * @return list of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam final String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }
}
