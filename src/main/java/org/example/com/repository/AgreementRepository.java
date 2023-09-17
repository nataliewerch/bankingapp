package org.example.com.repository;

import org.example.com.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Agreement entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {

    /**
     * Checks if an agreement exists for a specific account.
     *
     * @param accountId - the unique identifier of the account.
     * @return true if an agreement exists for the specified account, false otherwise.
     */
    boolean existsAgreementByAccount_Id(UUID accountId);
}
