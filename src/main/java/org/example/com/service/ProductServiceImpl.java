package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.ManagerRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ManagerRepository managerRepository;

    @Override
    public List<Product> getAll() {
        List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            throw new ManagerNotFoundException("No products found");
        }
        return products;
    }

    @Override
    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", id)));
    }

    @Override
    public Product create(Product product, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager not found with id %d: ", managerId)));
        product.setManager(manager);
        return repository.save(product);
    }
}
