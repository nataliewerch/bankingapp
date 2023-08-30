package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.entity.Account;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    @Override
    public AccountDto toDto(Account account) {
        return new AccountDto(account.getId(),
                account.getName(),
                account.getType(),
                account.getStatus(),
                account.getBalance(),
                account.getCurrencyCode(),
                account.getCreatedAt(), account.getUpdatedAt(),null, null,
                null);
    }

    @Override
    public Account toEntity(AccountDto accountDto) {
        return new Account(accountDto.getId(),
                accountDto.getName(),
                accountDto.getType(),
                accountDto.getStatus(),
                accountDto.getBalance(),
                accountDto.getCurrencyCode(),
                accountDto.getCreatedAt(),
                accountDto.getUpdatedAt(), null,
                null, null, null);
    }
}
