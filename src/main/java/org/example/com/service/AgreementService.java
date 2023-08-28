package org.example.com.service;

import org.example.com.dto.AgreementDto;

import java.util.List;
import java.util.UUID;

public interface AgreementService {

    List<AgreementDto> getAll();

    AgreementDto getById(Long id);

    AgreementDto create(AgreementDto agreementDto, UUID accountId, Long productId);

    void deleteByAccountId(UUID accountID);
}
