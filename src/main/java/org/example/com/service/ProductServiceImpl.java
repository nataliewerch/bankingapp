package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Product;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public List<Product> getAll() {
        return repository.findAll();
    }

    @Override
    public Product getById(Long id) {
        Product product = getById(id);
        if (product == null) {
            throw new ProductNotFoundException(String.format("Product with id %d not found", id));
        }
        return product;
    }

    @Override
    public Product create(Product product) {
        return repository.save(product);
    }
}
