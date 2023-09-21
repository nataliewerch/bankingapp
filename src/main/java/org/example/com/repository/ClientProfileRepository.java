package org.example.com.repository;

import org.example.com.entity.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ClientProfile entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    /**
     * Retrieves a client profile by their login.
     *
     * @param login the login of the client profile to retrieve.
     * @return the client profile with the specified login, or null if not found.
     */
    ClientProfile findByLogin(String login);

    /**
     * Checks if a client profile exists with the given login.
     *
     * @param login the login to check for existence.
     * @return true if a client profile with the specified login exists, false otherwise.
     */
    boolean existsByLogin(String login);
}
