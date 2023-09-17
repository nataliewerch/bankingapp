package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Account;
import org.example.com.entity.Agreement;
import org.example.com.entity.Product;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.AgreementAlreadyExistsException;
import org.example.com.exception.AgreementNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.AgreementRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the AgreementService interface for managing agreement-related operations.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Service
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository repository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    /**
     * Retrieves a list of all agreements.
     *
     * @return A list of Agreement objects.
     * @throws AgreementNotFoundException If no agreements are found in the database.
     */
    @Override
    public List<Agreement> getAll() {
        List<Agreement> agreements = repository.findAll();
        if (agreements.isEmpty()) {
            throw new AgreementNotFoundException("No agreements found");
        }
        return agreements;
    }

    /**
     * Retrieves an agreement by its unique identifier.
     *
     * @param id - The unique identifier of the agreement.
     * @return The Agreement object with the specified ID.
     * @throws AgreementNotFoundException If an agreement with the specified ID is not found in the database.
     */
    @Override
    public Agreement getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AgreementNotFoundException(String.format("Agreement with id %d not found", id)));
    }

    /**
     * Creates a new agreement and associates it with an account and a product.
     *
     * @param agreement The Agreement object to be created.
     * @param accountId The unique identifier of the account to which the agreement is associated.
     * @param productId The unique identifier of the product to which the agreement is associated.
     * @return The created Agreement object.
     * @throws AccountNotFoundException        If the specified account is not found in the database.
     * @throws ProductNotFoundException        If the specified product is not found in the database.
     * @throws AgreementAlreadyExistsException If an agreement already exists for the specified account.
     */
    @Override
    public Agreement create(Agreement agreement, UUID accountId, Long productId) {
        if (repository.existsAgreementByAccount_Id(accountId)) {
            throw new AgreementAlreadyExistsException(String.format("Agreement for account with id %s is already exist", accountId));
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", productId)));

        agreement.setAccount(account);
        agreement.setProduct(product);
        return repository.save(agreement);
    }

    /**
     * Deletes an agreement by its unique identifier.
     *
     * @param id - The unique identifier of the agreement to be deleted.
     * @throws AgreementNotFoundException If an agreement with the specified ID is not found in the database.
     */
    @Transactional
    @Override
    public void deleteById(Long id) {
        Agreement agreement = repository.findById(id)
                .orElseThrow(() -> new AgreementNotFoundException(String.format("Agreement with id %d not found", id)));
        repository.deleteById(id);
    }
}
