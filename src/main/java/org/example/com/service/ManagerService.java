package org.example.com.service;

import org.example.com.dto.ManagerDto;

import java.util.List;

public interface ManagerService {

    List<ManagerDto> getAll();

    ManagerDto getById(Long id);

    ManagerDto getWithClients(Long id);

    ManagerDto getWithProducts(Long id);

    ManagerDto create(ManagerDto manager);

    void deleteById(Long id);

    void reassignClients(Long sourceManagerId, Long targetManagerId);
    void reassignProducts(Long sourceManagerId, Long targetManagerId);
}
