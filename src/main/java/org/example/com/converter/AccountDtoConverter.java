package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Account;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Account entities and AccountDto DTOs.
 * This class provides methods for converting an account entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    /**
     * Converts an Account entity to an AccountDto DTO.
     *
     * @param account - The Account entity to convert.
     * @return The corresponding AccountDto DTO.
     */
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
                        new ClientDto(account.getClient().getId(), null, null, account.getClient().getFirstName(), account.getClient().getLastName(),
                                null, null, null, null, null, null, null, null, null),
                null,
                null);
    }

    /**
     * Converts an AccountDto DTO to an Account entity.
     *
     * @param accountDto - The AccountDto DTO to convert.
     * @return The corresponding Account entity.
     */
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
