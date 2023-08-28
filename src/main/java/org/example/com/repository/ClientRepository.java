package org.example.com.repository;

import org.example.com.entity.Client;
import org.example.com.entity.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    List<Client> getAllByStatus(ClientStatus status);
   List<Client> getAllByManager_Id(Long managerId);
}
