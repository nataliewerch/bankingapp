package org.example.com.repository;

import org.example.com.entity.Account;
import org.example.com.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Account entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    /**
     * Retrieves a list of accounts with the specified account status.
     *
     * @param accountStatus - the account status to filter by.
     * @return a list of accounts with the specified status.
     */
    List<Account> findAllByStatus(AccountStatus accountStatus);

    /**
     * Retrieves a list of accounts associated with a specific client.
     *
     * @param clientId - the unique identifier of the client.
     * @return a list of accounts associated with the client.
     */
    List<Account> findAllByClientId(UUID clientId);
}
