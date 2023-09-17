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

/**
 * Implementation of the ProductService interface for managing product-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ManagerRepository managerRepository;

    /**
     * Retrieves a list of all products.
     *
     * @return A list of Product objects.
     * @throws ProductNotFoundException If no products are found in the database.
     */
    @Override
    public List<Product> getAll() {
        List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return products;
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id - The unique identifier of the product.
     * @return The Product object with the specified ID.
     * @throws ProductNotFoundException If a product with the specified ID is not found in the database.
     */
    @Override
    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", id)));
    }

    /**
     * Retrieves a list of products managed by a manager with the specified ID.
     *
     * @param id - The unique identifier of the manager.
     * @return A list of Product objects managed by the specified manager.
     * @throws ManagerNotFoundException If a manager with the specified ID is not found in the database.
     * @throws ProductNotFoundException If no products are found for the specified manager.
     */
    @Override
    public List<Product> getAllByManagerId(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));
        List<Product> products = repository.getAllByManager_Id(id);
        if (products.isEmpty()) {
            throw new ProductNotFoundException(String.format("Product list is empty for manager with id %d", id));
        }
        return products;
    }

    /**
     * Creates a new product associated with a manager.
     *
     * @param product - The Product object to be created.
     * @param managerId - The unique identifier of the manager responsible for the product.
     * @return The created Product object.
     * @throws ManagerNotFoundException If a manager with the specified ID is not found in the database.
     */
    @Override
    public Product create(Product product, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager not found with id %d: ", managerId)));
        product.setManager(manager);
        return repository.save(product);
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id - The unique identifier of the product to be deleted.
     * @throws ProductNotFoundException If a product with the specified ID is not found in the database.
     */
    @Override
    public void deleteById(Long id) {
        Product product = getById(id);
        repository.delete(product);
    }
}
