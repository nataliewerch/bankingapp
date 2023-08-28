package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Product;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductDtoConverter implements Converter<Product, ProductDto> {
    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(),
                product.getName(),
                product.getStatus(),
                product.getCurrencyCode(),
                product.getInterestRate(),
                product.getLimit(),
                null, null, null, null);
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        return new Product(productDto.getId(),
                productDto.getName(),
                productDto.getStatus(),
                productDto.getCurrencyCode(),
                productDto.getInterestRate(),
                productDto.getLimit(),
                null, null, null, null);
    }
}
