package org.example.com.service;

import org.example.com.entity.Manager;

import java.util.List;

public interface ManagerService {

    List<Manager> getAll();

    Manager getById(Long id);

    Manager create(Manager manager);



    void delete(Manager manager);

    void deleteById(Long id);
}
