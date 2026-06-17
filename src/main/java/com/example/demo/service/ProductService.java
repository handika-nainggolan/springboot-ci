package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service class for Product business logic.
 */
@Service
public class ProductService {

    /** Product repository. */
    private final ProductRepository productRepository;

    /**
     * Constructor injection.
     *
     * @param productRepository the product repository
     */
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Get all products.
     *
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID.
     *
     * @param id the product ID
     * @return optional product
     */
    public Optional<Product> getProductById(final Long id) {
        return productRepository.findById(id);
    }

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    public Product createProduct(final Product product) {
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     *
     * @param id the product ID
     * @param updatedProduct the updated product data
     * @return the updated product or empty if not found
     */
    public Optional<Product> updateProduct(final Long id, final Product updatedProduct) {
        return productRepository.findById(id).map(existing -> {
            existing.setName(updatedProduct.getName());
            existing.setDescription(updatedProduct.getDescription());
            existing.setPrice(updatedProduct.getPrice());
            existing.setStock(updatedProduct.getStock());
            return productRepository.save(existing);
        });
    }

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     * @return true if deleted, false if not found
     */
    public boolean deleteProduct(final Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Search products by name.
     *
     * @param name the name to search
     * @return list of matching products
     */
    public List<Product> searchByName(final String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
