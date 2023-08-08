package org.example.com.repository;

import org.example.com.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
