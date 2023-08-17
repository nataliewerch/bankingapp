package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
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
    public Manager getById(long id) {
        return managerRepository.getReferenceById(id);
    }

    @Override
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public void delete(Manager manager) {
        managerRepository.delete(manager);
    }

    @Override
    public void deleteById(long id) {
        managerRepository.deleteById(id);
    }
}
