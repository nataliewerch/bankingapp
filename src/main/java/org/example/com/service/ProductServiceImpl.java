package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.exception.ProductNotFoundException;
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
        List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            throw new ManagerNotFoundException("No products found");
        }
        return products.stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        Product product= repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %d not found", id)));
        return productDtoConverter.toDto(product);
    }

    @Override
    public ProductDto create(ProductDto productDto, Long managerId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager not found with id %d: " + managerId)));
        Product product = productDtoConverter.toEntity(productDto);
        product.setManager(manager);
        Product createdProduct = repository.save(product);
        return productDtoConverter.toDto(createdProduct);
    }
}
