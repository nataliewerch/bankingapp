package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Account;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    private final TransactionDtoConverter transactionConverter;

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
                account.getClient().getId(), account.getClient().getFirstName(), account.getClient().getLastName(), transactionDtoList);
    }

    @Override
    public Account toEntity(AccountDto accountDto) {

        throw new UnsupportedOperationException();
    }
}
