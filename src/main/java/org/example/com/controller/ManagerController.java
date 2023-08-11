package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.Manager;
import org.example.com.service.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("managers")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public List<Manager> getAll() {
        return managerService.getAll();
    }

    @PostMapping
    public Manager create(@RequestBody Manager manager) {
        return managerService.createManager(manager);
    }
}
