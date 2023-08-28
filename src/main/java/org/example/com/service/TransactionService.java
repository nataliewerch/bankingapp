package org.example.com.service;

import org.example.com.dto.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    List<TransactionDto> getAll();

    TransactionDto getById(UUID id);

    TransactionDto create(TransactionDto transactionDto);

    List<TransactionDto> findByAccountId(UUID accountId);
}
