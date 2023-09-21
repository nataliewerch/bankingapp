package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.exception.ProductNotFoundException;
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
    private final ManagerService managerService;

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
     * @param id The unique identifier of the product.
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
     * @param id The unique identifier of the manager.
     * @return A list of Product objects managed by the specified manager.
     * @throws ProductNotFoundException If no products are found for the specified manager.
     */
    @Override
    public List<Product> getAllByManagerId(Long id) {
        Manager manager = managerService.getById(id);
        List<Product> products = repository.getAllByManager_Id(manager.getId());

        if (products.isEmpty()) {
            throw new ProductNotFoundException(String.format("Product list is empty for manager with id %d", id));
        }
        return products;
    }

    /**
     * Creates a new product associated with a manager.
     *
     * @param product   The Product object to be created.
     * @param managerId The unique identifier of the manager responsible for the product.
     * @return The created Product object.
     * @throws ManagerNotFoundException If a manager with the specified ID is not found in the database.
     */
    @Override
    public Product create(Product product, Long managerId) {
        Manager manager = managerService.getById(managerId);
        product.setManager(manager);
        return repository.save(product);
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The unique identifier of the product to be deleted.
     * @throws ProductNotFoundException If a product with the specified ID is not found in the database.
     */
    @Override
    public void deleteById(Long id) {
        Product product = getById(id);
        repository.delete(product);
    }

    /**
     * Reassigns products from one manager to another.
     *
     * @param sourceManagerId The unique identifier of the source manager.
     * @param targetManagerId The unique identifier of the target manager.
     * @throws ProductNotFoundException If no products are found for reassignment.
     */
    @Override
    public void reassignProducts(Long sourceManagerId, Long targetManagerId) {
        List<Product> productsToReassign = repository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = managerService.getById(targetManagerId);

        if (productsToReassign != null && !productsToReassign.isEmpty()) {
            for (Product product : productsToReassign) {
                product.setManager(targetManager);
                repository.save(product);
            }
        } else {
            throw new ProductNotFoundException("No products found");
        }
    }
}
