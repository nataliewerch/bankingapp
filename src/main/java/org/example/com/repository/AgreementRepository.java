package org.example.com.repository;

import org.example.com.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    void deleteAgreementByAccount_Id(UUID accountId);
}
