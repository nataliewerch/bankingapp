package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Account;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    @Override
    public AccountDto toDto(Account account) {
        return new AccountDto(null,
                account.getName(),
                account.getType(),
                account.getStatus(),
                account.getBalance(),
                account.getCurrencyCode(),
                account.getCreatedAt(), account.getUpdatedAt(),
                account.getClient() == null ? null :
                        new ClientDto(null, null, null, account.getClient().getFirstName(), account.getClient().getLastName(),
                                null, null, null, null, null, null, null, null),
                null,
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
                accountDto.getUpdatedAt(),
                null, null, null, null);
    }
}
