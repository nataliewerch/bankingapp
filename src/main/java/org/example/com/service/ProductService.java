package org.example.com.service;

import org.example.com.entity.Product;

import java.util.List;

/**
 * This interface defines the contract for managing product-related operations.
 *
 * @author Natalie Werch
 */
public interface ProductService {
    List<Product> getAll();

    Product getById(Long id);

    List<Product> getAllByManagerId(Long id);

    Product create(Product product, Long managerId);

    void deleteById(Long id);
}
