package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ManagerRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ManagerRepository managerRepository;
    private final Converter<Product, ProductDto> productDtoConverter;

    @Override
    public List<ProductDto> getAll() {
        return repository.findAll().stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        return productDtoConverter.toDto(repository.getReferenceById(id));
    }

    @Override
    public ProductDto create(ProductDto productDto, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found with id: " + managerId));
        Product product = productDtoConverter.toEntity(productDto);
        product.setManager(manager);
        Product createdProduct = repository.save(product);
        return productDtoConverter.toDto(createdProduct);
    }
}
