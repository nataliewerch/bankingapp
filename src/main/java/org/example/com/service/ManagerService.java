package org.example.com.service;

import org.example.com.entity.Manager;

import java.util.List;

public interface ManagerService {

    List<Manager> getAll();

    Manager getById(long id);

    Manager createManager(Manager manager);

    void delete(Manager manager);

    void deleteById(long id);
}
