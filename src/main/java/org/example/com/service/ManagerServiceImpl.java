package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.*;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the ManagerService interface for managing manager-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves a list of all managers.
     *
     * @return A list of Manager objects.
     * @throws ManagerNotFoundException If no managers are found in the database.
     */
    @Override
    public List<Manager> getAll() {
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) {
            throw new ManagerNotFoundException("No managers found");
        }
        return managers;
    }

    /**
     * Retrieves a manager by their unique identifier.
     *
     * @param id - The unique identifier of the manager.
     * @return The Manager object with the specified ID.
     * @throws ManagerNotFoundException If a manager with the specified ID is not found in the database.
     */
    @Override
    public Manager getById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));
    }

    /**
     * Creates a new manager.
     *
     * @param manager - The Manager object to be created.
     * @return The created Manager object.
     */
    @Override
    public Manager create(Manager manager) {
        return managerRepository.save(manager);
    }

    /**
     * Deletes a manager by their unique identifier.
     *
     * @param id - The unique identifier of the manager to be deleted.
     * @throws ManagerNotFoundException    If the specified manager is not found in the database.
     * @throws ManagerHasClientsException  If the manager has assigned clients.
     * @throws ManagerHasProductsException If the manager has assigned products.
     */
    @Override
    public void deleteById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));

        if (!manager.getClients().isEmpty()) {
            throw new ManagerHasClientsException(String.format("Manager with id: %d  has assigned clients. Reassign clients before deleting the manager!!!", id));
        }
        if (!manager.getProducts().isEmpty()) {
            throw new ManagerHasProductsException(String.format("Manager with id: %d  has assigned products. Reassign products before deleting the manager!!!", id));
        }
        managerRepository.deleteById(id);
    }

    /**
     * Reassigns clients from one manager to another.
     *
     * @param sourceManagerId - The unique identifier of the source manager.
     * @param targetManagerId - The unique identifier of the target manager.
     * @throws ClientNotFoundException If no clients are found for reassignment.
     */
    @Override
    public void reassignClients(Long sourceManagerId, Long targetManagerId) {
        List<Client> clientsToReassign = clientRepository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = getById(targetManagerId);

        if (clientsToReassign != null && !clientsToReassign.isEmpty()) {
            for (Client client : clientsToReassign) {
                client.setManager(targetManager);
                clientRepository.save(client);
            }
        } else {
            throw new ClientNotFoundException("No clients found");
        }
    }

    /**
     * Reassigns products from one manager to another.
     *
     * @param sourceManagerId - The unique identifier of the source manager.
     * @param targetManagerId - The unique identifier of the target manager.
     * @throws ProductNotFoundException If no products are found for reassignment.
     */
    @Override
    public void reassignProducts(Long sourceManagerId, Long targetManagerId) {
        List<Product> productsToReassign = productRepository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = getById(targetManagerId);

        if (productsToReassign != null && !productsToReassign.isEmpty()) {
            for (Product product : productsToReassign) {
                product.setManager(targetManager);
                productRepository.save(product);
            }
        } else {
            throw new ProductNotFoundException("No products found");
        }
    }
}
