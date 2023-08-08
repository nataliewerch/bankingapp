package org.example.com.repository;

import org.example.com.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
