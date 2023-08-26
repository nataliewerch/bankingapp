package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AgreementDto;
import org.example.com.entity.Agreement;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AgreementDtoConverter implements Converter<Agreement, AgreementDto> {
    @Override
    public AgreementDto toDto(Agreement agreement) {
        return new AgreementDto(agreement.getId(),
                agreement.getInterestRate(),
                agreement.getStatus(),
                agreement.getSum(),
                null, null);
    }

    @Override
    public Agreement toEntity(AgreementDto agreementDto) {
        return new Agreement(agreementDto.getId(),
                agreementDto.getInterestRate(),
                agreementDto.getStatus(),
                agreementDto.getSum(),
                null,null,null,null);
    }
}
