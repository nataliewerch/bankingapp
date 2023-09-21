package org.example.com.service;

import org.example.com.entity.ClientProfile;

/**
 * This interface defines the contract for managing client profiles.
 *
 * @author Natalie Werch
 */

public interface ClientProfileService {

    ClientProfile create(ClientProfile clientProfile);

    ClientProfile getByLogin(String login);

    boolean existsByLogin(String login);
}
