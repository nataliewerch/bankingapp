package org.example.com.repository;

import org.example.com.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
