package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.service.ManagerService;
import org.example.com.converter.Converter;
import org.example.com.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ProductService productService;
    private final Converter<Manager, ManagerDto> converter;
    private final Converter<Product, ProductDto> productDtoConverter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

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


    @PostMapping
    ResponseEntity<Manager> create(@RequestBody Manager managerDto) {
        try {
            logger.info("ЗАПРОС ПОЛУЧЕН: {}", managerDto);

            Manager createdManager = managerService.create(managerDto);


            logger.info("ОПЕРАЦИЯ ПРОШЛА УСПЕШНО: {}", createdManager);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdManager);
        } catch (Exception e) {
            logger.error("ОШИБКААА", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/products")
    ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {

        try {
            logger.info("ЗАПРОС ПОЛУЧЕН: {}", productDto);

            Product product = productDtoConverter.toEntity(productDto);
            Product createdProduct = productService.create(product);
            ProductDto createdProductDto = productDtoConverter.toDto(createdProduct);

            logger.info("ОПЕРАЦИЯ ПРОШЛА УСПЕШНО: {}", product);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDto);
        } catch (Exception e) {
            logger.error("ОШИБКААА", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/products")
    List<ProductDto> getAllProducts() {
        return productService.getAll().stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
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
