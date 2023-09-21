package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Product;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Product entities and ProductDto DTOs.
 * This class provides methods for converting a product entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class ProductDtoConverter implements Converter<Product, ProductDto> {

    /**
     * Converts a Product entity to a ProductDto DTO.
     *
     * @param product - The Product entity to convert.
     * @return The corresponding ProductDto DTO.
     */
    @Override
    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(),
                product.getName(),
                product.getStatus(),
                product.getCurrencyCode(),
                product.getInterestRate(),
                product.getLimit());
    }

    /**
     * Converts a ProductDto DTO to a Product entity.
     *
     * @param productDto - The ProductDto DTO to convert.
     * @return The corresponding Product entity.
     */
    @Override
    public Product toEntity(ProductDto productDto) {
        return new Product(productDto.getId(),
                productDto.getName(),
                productDto.getStatus(),
                productDto.getCurrencyCode(),
                productDto.getInterestRate(),
                productDto.getLimit(), null, null, null, null);
    }
}
