package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AgreementDto;
import org.example.com.entity.Account;
import org.example.com.entity.Agreement;
import org.example.com.entity.Product;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.AgreementNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.AccountRepository;
import org.example.com.repository.AgreementRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository repository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final Converter<Agreement, AgreementDto> agreementDtoConverter;

    @Override
    public List<AgreementDto> getAll() {
        List<Agreement> agreements = repository.findAll();
        if (agreements.isEmpty()) {
            throw new AgreementNotFoundException("No agreements found");
        }
        return agreements.stream()
                .map(agreementDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AgreementDto getById(Long id) {
        return agreementDtoConverter.toDto(repository.findById(id)
                .orElseThrow(()->new AgreementNotFoundException(String.format("Agreement with id %d not found", id))));
    }

    @Override
    public AgreementDto create(AgreementDto agreementDto, UUID accountId, Long productId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountId)));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", productId)));

        Agreement agreement = agreementDtoConverter.toEntity(agreementDto);
        agreement.setAccount(account);
        agreement.setProduct(product);
        Agreement createdAgreement = repository.save(agreement);
        return agreementDtoConverter.toDto(createdAgreement);
    }

    @Override
    public void deleteByAccountId(UUID accountID) {
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %s not found", accountID)));

        repository.deleteAgreementByAccount_Id(accountID);
    }
}
