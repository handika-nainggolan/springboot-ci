package com.example.demo.repository;

import com.example.demo.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by name containing the given string (case-insensitive).
     *
     * @param name the name to search for
     * @return list of matching products
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
