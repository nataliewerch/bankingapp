package org.example.com.repository;

import org.example.com.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Transaction entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Retrieves a list of transactions associated with a specified debit or credit account.
     *
     * @param debitAccountId - the unique identifier of the debit account.
     * @param creditAccountId - the unique identifier of the credit account.
     * @return a list of transactions associated with the specified debit or credit account.
     */
    List<Transaction> findByAccountDebitIdOrAccountCreditId(UUID debitAccountId, UUID creditAccountId);
}
