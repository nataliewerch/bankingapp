package org.example.com.repository;

import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Client entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Retrieves a list of clients with the specified status.
     *
     * @param status - the status of clients to retrieve.
     * @return a list of clients with the specified status.
     */
    List<Client> getAllByStatus(ClientStatus status);

    /**
     * Retrieves a list of clients managed by a manager with the specified identifier.
     *
     * @param managerId - the unique identifier of the manager.
     * @return a list of clients managed by the manager with the specified identifier.
     */
    List<Client> getAllByManager_Id(Long managerId);
}
