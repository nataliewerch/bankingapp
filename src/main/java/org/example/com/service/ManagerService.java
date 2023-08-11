package org.example.com.service;

import org.example.com.entity.Manager;

import java.util.List;

public interface ManagerService {

    List<Manager> getAll();

    Manager createManager(Manager manager);
}
