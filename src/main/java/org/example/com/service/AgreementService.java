package org.example.com.service;

import org.example.com.dto.AgreementDto;
import org.example.com.entity.Agreement;

import java.util.List;
import java.util.UUID;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(Long id);

    Agreement create(Agreement agreement, UUID accountId, Long productId);

    void deleteByAccountId(UUID accountID);
}
