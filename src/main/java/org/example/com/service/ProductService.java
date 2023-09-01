package org.example.com.service;

import org.example.com.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product getById(Long id);

    Product create(Product product, Long managerId);
}
