package org.example.com.repository;

import org.example.com.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
}
