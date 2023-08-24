package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Agreement;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AgreementDtoConverter implements Converter<Agreement, AgreementDto> {

    private AccountDtoConverter accountDtoConverter;
    private ProductDtoConverter productDtoConverter;

    @Override
    public AgreementDto toDto(Agreement agreement) {
        return new AgreementDto(agreement.getId(), agreement.getInterestRate(), agreement.getStatus(), agreement.getSum(),
                new AccountDto(agreement.getAccount().getId(), agreement.getAccount().getName(), agreement.getAccount().getType(), null, null, null, null, null),
                new ProductDto(agreement.getProduct().getId(), agreement.getProduct().getName(), agreement.getProduct().getStatus(), agreement.getProduct().getCurrencyCode(), agreement.getProduct().getInterestRate(), agreement.getProduct().getLimit(), null, null));
    }

    @Override
    public Agreement toEntity(AgreementDto agreementDto) {
        return new Agreement(agreementDto.getId(),
                agreementDto.getInterestRate(),
                agreementDto.getStatus(),
                agreementDto.getSum(),
                null, null,
                accountDtoConverter.toEntity(agreementDto.getAccount()),
                productDtoConverter.toEntity(agreementDto.getProduct()));
    }
}
