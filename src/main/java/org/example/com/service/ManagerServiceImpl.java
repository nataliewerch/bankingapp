package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
import org.example.com.entity.ManagerProfile;
import org.example.com.exception.*;
import org.example.com.repository.ManagerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ManagerProfileService managerProfileService;
    private final PasswordEncoder passwordEncoder;

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
     * @param id The unique identifier of the manager.
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
     * @param manager The Manager object to be created.
     * @return The created Manager object.
     */
    @Override
    @Transactional
    public Manager create(Manager manager, String login, String password) {
        managerRepository.save(manager);
        managerProfileService.create(
                new ManagerProfile(login, passwordEncoder.encode(password), manager.getId()));
        return manager;
    }

    /**
     * Deletes a manager by their unique identifier.
     *
     * @param id The unique identifier of the manager to be deleted.
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
}
