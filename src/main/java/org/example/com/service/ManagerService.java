package org.example.com.service;

import org.example.com.entity.Manager;

import java.util.List;

/**
 * This interface defines the contract for managing manager-related operations.
 *
 * @author Natalie Werch
 */
public interface ManagerService {

    List<Manager> getAll();

    Manager getById(Long id);

    Manager create(Manager manager);

    void deleteById(Long id);

    void reassignClients(Long sourceManagerId, Long targetManagerId);

    void reassignProducts(Long sourceManagerId, Long targetManagerId);
}
