package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Agreement;
import org.example.com.exception.AgreementNotFoundException;
import org.example.com.repository.AgreementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(AgreementServiceImpl.class);

    @Override
    public List<Agreement> getAll() {
        return repository.findAll();
    }

    @Override
    public Agreement getById(Long id) {
        Agreement agreement = repository.getReferenceById(id);
        if (agreement == null) {
            throw new AgreementNotFoundException(String.format("Agreement with id %d not found", id));
        }
        return agreement;
    }

    @Override
    public Agreement create(Agreement agreement) {

        return repository.save(agreement);
    }

    @Override
    public void deleteByAccountId(UUID accountID) {
        logger.info("УДАЛЕНИЕ account ID: {}", accountID);
        repository.deleteAgreementByAccount_Id(accountID);
        logger.info("УЛАЛЕН account ID: {}", accountID);
    }
}
