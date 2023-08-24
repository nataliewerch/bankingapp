package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Account;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    private final TransactionDtoConverter transactionConverter;
    private final ClientDtoConverter clientDtoConverter;

    @Override
    public AccountDto toDto(Account account) {
        Set<TransactionDto> allTransactions = new HashSet<>();
        allTransactions.addAll(account.getDebitTransactions().stream()
                .map(transactionConverter::toDto)
                .toList());
        allTransactions.addAll(account.getCreditTransactions().stream()
                .map(transactionConverter::toDto)
                .toList());

        List<TransactionDto> transactionDtoList = new ArrayList<>(allTransactions);

        return new AccountDto(account.getId(), account.getName(), account.getType(), account.getStatus(), account.getBalance(), account.getCurrencyCode(),
                new ClientDto(null, null, null, account.getClient().getFirstName(), account.getClient().getLastName(), null, null, null, null, null, null), transactionDtoList);

    }

    @Override
    public Account toEntity(AccountDto accountDto) {
        return new Account(accountDto.getId(),
                accountDto.getName(),
                accountDto.getType(),
                accountDto.getStatus(),
                accountDto.getBalance(),
                accountDto.getCurrencyCode(), null, null,
                clientDtoConverter.toEntity(accountDto.getClient()),
                accountDto.getTransactions().stream()
                        .map(transactionConverter::toEntity)
                        .collect(Collectors.toList()),
                null);
    }
}
