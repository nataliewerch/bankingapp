package org.example.com.repository;

import org.example.com.entity.Account;
import org.example.com.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAllByStatus(AccountStatus accountStatus);

    List<Account> findAllByClientId(UUID clientId);
}
