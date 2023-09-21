package org.example.com.service;

import org.example.com.entity.ManagerProfile;


/**
 * This interface defines the contract for managing manager profiles.
 *
 * @author Natalie Werch
 */
public interface ManagerProfileService {

    ManagerProfile create(ManagerProfile managerProfile);

    ManagerProfile getByLogin(String login);
}
