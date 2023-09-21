package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.ClientProfile;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.exception.LoginAlreadyExistsException;
import org.example.com.repository.ClientProfileRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the ClientProfileService interface for managing clientProfile-related operations.
 *
 * @author Natalie Werch
 */
@Service
@RequiredArgsConstructor
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository repository;

    /**
     * Creates a new client profile.
     *
     * @param clientProfile - The ClientProfile object to be created.
     * @return The created ClientProfile object.
     * @throws LoginAlreadyExistsException If a client profile with the same login already exists in the database.
     */
    @Override
    public ClientProfile create(ClientProfile clientProfile) {
        String login = clientProfile.getLogin();
        if (repository.existsByLogin(login)) {
            throw new LoginAlreadyExistsException(String.format("Client with login %s is already exist!", login));
        }
        return repository.save(clientProfile);
    }

    /**
     * Retrieves a client profile by its login.
     *
     * @param login - The login identifier associated with the client profile.
     * @return The ClientProfile object with the specified login.
     * @throws ClientNotFoundException If a client profile with the specified login is not found in the database.
     */
    @Override
    public ClientProfile getByLogin(String login) {
        return repository.findByLogin(login);
    }
}
