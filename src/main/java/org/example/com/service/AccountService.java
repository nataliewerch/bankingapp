package org.example.com.service;

import org.example.com.dto.AccountDto;
import org.example.com.dto.TransactionDto;;
import org.example.com.entity.enums.AccountStatus;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto> getAll();

    AccountDto getById(UUID id);

    List<AccountDto> getByStatus(AccountStatus accountStatus);

   List<AccountDto> getByClientId(UUID clientId);

    Double balance(UUID id);

    AccountDto create(AccountDto accountDto, UUID clientId);

    AccountDto deposit(UUID id, Double amount, String description);

    AccountDto withdraw(UUID id, Double amount, String description);

    TransactionDto transfer(UUID senderId, UUID receiverId, Double amount, String description);

    List<TransactionDto> getTransactionHistory(UUID id);

    void deleteById(UUID id);

}
