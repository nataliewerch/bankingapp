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
        return repository.findAll().stream()
                .map(agreementDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AgreementDto getById(Long id) {
        AgreementDto agreementDto = agreementDtoConverter.toDto(repository.getReferenceById(id));
        if (agreementDto == null) {
            throw new AgreementNotFoundException(String.format("Agreement with id %d not found", id));
        }
        return agreementDto;
    }

    @Override
    public AgreementDto create(AgreementDto agreementDto, UUID accountId, Long productId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        Agreement agreement = agreementDtoConverter.toEntity(agreementDto);
        agreement.setAccount(account);
        agreement.setProduct(product);
        Agreement createdAgreement = repository.save(agreement);
        return agreementDtoConverter.toDto(createdAgreement);
    }

    @Override
    public void deleteByAccountId(UUID accountID) {
        repository.deleteAgreementByAccount_Id(accountID);
    }
}
