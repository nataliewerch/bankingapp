package org.example.com.service;

import org.example.com.entity.Agreement;

import java.util.List;
import java.util.UUID;

/**
 * This interface defines the contract for managing agreements.
 *
 * @author Natalie Werch
 */
public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(Long id);

    Agreement create(Agreement agreement, UUID accountId, Long productId);
}
