package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Agreement;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.service.AgreementService;
import org.example.com.service.ManagerService;
import org.example.com.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("managers")
public class ManagerController {

    private final ManagerService managerService;
    private final ProductService productService;
    private final AgreementService agreementService;
    private final Converter<Manager, ManagerDto> managerDtoConverter;
    private final Converter<Product, ProductDto> productDtoConverter;
    private final Converter<Agreement, AgreementDto> agreementDtoConverter;

    @GetMapping
    List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    ManagerDto getById(@PathVariable(name = "id") Long id) {
        return managerDtoConverter.toDto(managerService.getById(id));
    }

    @GetMapping("/clients/{managerId}")
    ManagerDto getManagerWithClients(@PathVariable(name = "managerId") Long managerId) {
        return managerService.getWithClients(managerId);
    }

    @GetMapping("/products/{managerId}")
    ManagerDto getManagerWithProducts(@PathVariable(name = "managerId") Long managerId) {
        return managerService.getWithProducts(managerId);
    }

    @PostMapping
    ManagerDto createManager(@RequestBody ManagerDto managerDto) {
        return managerDtoConverter.toDto(managerService.create(managerDtoConverter.toEntity(managerDto)));
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
        return productDtoConverter.toDto(productService.create(productDtoConverter.toEntity(productDto), managerId));
    }

    @GetMapping("/products")
    List<ProductDto> getAllProducts() {
        return productService.getAll().stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/agreements/create/{accountId}/{productId}")
    AgreementDto createAgreement(@RequestBody AgreementDto agreementDto,
                                 @PathVariable(name = "accountId") UUID accountId,
                                 @PathVariable(name = "productId") Long productId) {
        return agreementDtoConverter.toDto(agreementService.create(agreementDtoConverter.toEntity(agreementDto), accountId, productId));
    }
}
