package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.entity.Account;
import org.example.com.entity.Agreement;
import org.example.com.entity.Product;
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
                accountDto.getUpdatedAt(),
                accountDto.getAgreementDto()==null ? null :
                new Agreement(accountDto.getAgreementDto().getId(),
                        accountDto.getAgreementDto().getInterestRate(),
                        accountDto.getAgreementDto().getStatus(),
                        accountDto.getAgreementDto().getSum(),
                        accountDto.getAgreementDto().getCreatedAt(),
                        accountDto.getAgreementDto().getUpdatedAt(),
                        null,
                        new Product(accountDto.getAgreementDto().getProductDto().getId(),
                                accountDto.getAgreementDto().getProductDto().getName(),
                                accountDto.getAgreementDto().getProductDto().getStatus(),
                                accountDto.getAgreementDto().getProductDto().getCurrencyCode(),
                                accountDto.getAgreementDto().getProductDto().getInterestRate(),
                                accountDto.getAgreementDto().getProductDto().getLimit(),
                                accountDto.getAgreementDto().getProductDto().getCreatedAt(),
                                accountDto.getAgreementDto().getProductDto().getUpdatedAt(),
                                null, null)),
                null, null, null);
    }
}
