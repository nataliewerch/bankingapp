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
        return new AccountDto(account.getId(),
                account.getName(),
                account.getType(),
                account.getStatus(),
                account.getBalance(),
                account.getCurrencyCode(),
                null,
//                new ClientDto(account.getClient().getId(),
//                        account.getClient().getStatus(),
//                        account.getClient().getTaxCode(),
//                        account.getClient().getFirstName(),
//                        account.getClient().getLastName(),
//                        account.getClient().getEmail(),
//                        account.getClient().getAddress(),
                //                       account.getClient().getPhone(),
                //              null, null, null),
                null);

    }

    @Override
    public Account toEntity(AccountDto accountDto) {

        ClientDto clientDto = accountDto.getClient();
        if (clientDto == null) {
            throw new IllegalArgumentException("Client must be provided in AccountDto.");
        }

        return new Account(accountDto.getId(),
                accountDto.getName(),
                accountDto.getType(),
                accountDto.getStatus(),
                accountDto.getBalance(),
                accountDto.getCurrencyCode(),
                null, null, null,
//                new Client(accountDto.getClient().getId(),
//                        accountDto.getClient().getStatus(),
//                        accountDto.getClient().getTaxCode(),
//                        accountDto.getClient().getFirstName(),
//                        accountDto.getClient().getLastName(),
//                        accountDto.getClient().getEmail(),
//                        accountDto.getClient().getAddress(),
//                        accountDto.getClient().getPhone(),
//                        null, null,
//                        null, null),
                        null, null);
    }
}
