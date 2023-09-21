package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AgreementDto;
import org.example.com.entity.Agreement;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Agreement entities and AgreementDto DTOs.
 * This class provides methods for converting an agreement entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class AgreementDtoConverter implements Converter<Agreement, AgreementDto> {

    /**
     * Converts an Agreement entity to an AgreementDto DTO.
     *
     * @param agreement - The Agreement entity to convert.
     * @return The corresponding AgreementDto DTO.
     */
    @Override
    public AgreementDto toDto(Agreement agreement) {
        return new AgreementDto(agreement.getId(),
                agreement.getInterestRate(),
                agreement.getStatus(),
                agreement.getSum());
    }

    /**
     * Converts an AgreementDto DTO to an Agreement entity.
     *
     * @param agreementDto - The AgreementDto DTO to convert.
     * @return The corresponding Agreement entity.
     */
    @Override
    public Agreement toEntity(AgreementDto agreementDto) {
        return new Agreement(agreementDto.getId(),
                agreementDto.getInterestRate(),
                agreementDto.getStatus(),
                agreementDto.getSum(),
                null, null,
                null, null);
    }
}
