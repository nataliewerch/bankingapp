package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.service.ManagerService;
import org.example.com.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("managers")
public class ManagerController {

    private final ManagerService managerService;
    private final ProductService productService;

    @GetMapping
    List<ManagerDto> getAll() {
        return managerService.getAll();
    }

    @GetMapping("/{id}")
    ManagerDto getById(@PathVariable(name = "id") Long id) {
        return managerService.getById(id);
    }

    @GetMapping("/clients/{id}")
    ManagerDto getManagerWithClients(@PathVariable(name = "id") Long id) {
        return managerService.getWithClients(id);
    }

    @PostMapping
    ManagerDto createManager(@RequestBody ManagerDto managerDto) {
        return managerService.create(managerDto);
    }

    @DeleteMapping("/delete/{id}")
    void deleteManagerById(@PathVariable(name = "id") Long id) {
        managerService.deleteById(id);
    }


    @PostMapping("/reassign-clients/{sourceManagerId}/{targetManagerId}")
    public void reassignClients(@PathVariable(name = "sourceManagerId") Long sourceManagerId,
                                @PathVariable(name = "targetManagerId") Long targetManagerId) {
        managerService.reassignClients(sourceManagerId, targetManagerId);
    }

    @PostMapping("/reassign-products/{sourceManagerId}/{targetManagerId}")
    public void reassignProducts(@PathVariable(name = "sourceManagerId") Long sourceManagerId,
                                @PathVariable(name = "targetManagerId") Long targetManagerId) {
        managerService.reassignProducts(sourceManagerId, targetManagerId);
    }


    @PostMapping("/products/create/{managerId}")
    ProductDto createProduct(@RequestBody ProductDto productDto,
                             @PathVariable(name = "managerId") Long managerId) {
        return productService.create(productDto, managerId);
    }

    @GetMapping("/products")
    List<ProductDto> getAllProducts() {
        return productService.getAll();
    }

}
