package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.ManagerProfile;
import org.example.com.exception.LoginAlreadyExistsException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ManagerProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ManagerProfileService interface for managing managerProfile-related operations.
 *
 * @author Natalie Werch
 */
@Service
@RequiredArgsConstructor
public class ManagerProfileServiceImpl implements ManagerProfileService {

    private final ManagerProfileRepository repository;

    /**
     * Creates a new manager profile.
     *
     * @param managerProfile - The ManagerProfile object to be created.
     * @return The created ManagerProfile object.
     * @throws LoginAlreadyExistsException If a manager with the same login already exists in the database.
     */
    @Override
    public ManagerProfile create(ManagerProfile managerProfile) {
        String login = managerProfile.getLogin();
        if (repository.existsByLogin(login)) {
            throw new LoginAlreadyExistsException(String.format("Manager with login %s is already exist!", login));
        }
        return repository.save(managerProfile);
    }

    /**
     * Retrieves a manager profile by its login.
     *
     * @param login - The login identifier associated with the manager profile.
     * @return The ManagerProfile object with the specified login.
     * @throws ManagerNotFoundException If a manager with the specified login is not found in the database.
     */
    @Override
    public ManagerProfile getByLogin(String login) {
        return repository.findByLogin(login);
    }

    /**
     * Checks if a manager profile with the given login exists.
     *
     * @param login - The login identifier to check for existence.
     * @return True if a manager profile with the specified login exists, otherwise false.
     */
    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }
}
