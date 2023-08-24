package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    @Override
    public List<Manager> getAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getById(Long id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (manager == null) {
            throw new ManagerNotFoundException(String.format("Manager with id %d not found", id));
        }
        return manager;
    }

    @Override
    public Manager create(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public void delete(Manager manager) {
        managerRepository.delete(manager);
    }

    @Override
    public void deleteById(Long id) {
        managerRepository.deleteById(id);
    }
}
