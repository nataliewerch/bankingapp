package org.example.com.service;

import org.example.com.entity.Agreement;

import java.util.List;
import java.util.UUID;

public interface AgreementService {

    List<Agreement> getAll();

    Agreement getById(Long id);

    Agreement create(Agreement agreement);

    void deleteByAccountId(UUID accountID);
}
