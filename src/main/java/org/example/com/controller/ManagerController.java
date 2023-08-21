package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.entity.Manager;
import org.example.com.service.ManagerService;
import org.example.com.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("managers")
public class ManagerController {

    private final ManagerService managerService;
    private final Converter<Manager, ManagerDto> converter;
    // private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @GetMapping
    List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    ManagerDto getById(@PathVariable(name = "id") Long id) {
        return converter.toDto(managerService.getById(id));
    }

    // Этот метод выбрасывает UnsupportedOperationException,
    // я так понимаю, нужно будет реализовать метод toEntity в DTOConverter
    @PostMapping
    ResponseEntity<ManagerDto> create(@RequestBody ManagerDto managerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toDto(managerService.create(converter.toEntity(managerDto))));
    }

    @DeleteMapping("/delete")
    void deleteAccount(@RequestBody Manager manager) {
        managerService.delete(manager);
    }

    @DeleteMapping("/delete/{id}")
    void deleteAccountById(@PathVariable(name = "id") Long id) {
        managerService.deleteById(id);
    }
}
