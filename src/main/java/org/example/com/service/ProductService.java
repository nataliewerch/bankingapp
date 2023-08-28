package org.example.com.service;

import org.example.com.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll();

    ProductDto getById(Long id);

    ProductDto create(ProductDto productDto, Long managerId);
}
