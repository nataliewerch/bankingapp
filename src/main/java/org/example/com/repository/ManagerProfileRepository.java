package org.example.com.repository;

import org.example.com.entity.ManagerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing ManagerProfile entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface ManagerProfileRepository extends JpaRepository<ManagerProfile, Long> {

    /**
     * Retrieves a manager profile by its login.
     *
     * @param login - the login of the manager profile to retrieve.
     * @return a manager profile with the specified login or null if not found.
     */
    ManagerProfile findByLogin(String login);

    /**
     * Checks if a manager profile with the specified login exists.
     *
     * @param login - the login to check for existence.
     * @return true if a manager profile with the specified login exists; otherwise, false.
     */
    boolean existsByLogin(String login);
}
