package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Transaction;
import org.example.com.exception.TransactionNotFoundException;
import org.example.com.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final Converter<Transaction, TransactionDto> transactionDtoConverter;

    @Override
    public List<TransactionDto> getAll() {
        return repository.findAll().stream()
                .map(transactionDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(UUID id) {
        Transaction transaction = repository.getReferenceById(id);
        if (transaction == null) {
            throw new TransactionNotFoundException(String.format("Transaction with id %s not found", id));
        }
        return transactionDtoConverter.toDto(transaction);
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        return transactionDtoConverter.toDto(repository.save(transactionDtoConverter.toEntity(transactionDto)));
    }

    @Override
    public List<TransactionDto> findByAccountId(UUID accountId) {
        return repository.findByAccountDebitIdOrAccountCreditId(accountId, accountId).stream()
                .map(transactionDtoConverter::toDto)
                .collect(Collectors.toList());
    }
}
