package org.example.com.converter;

import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Product;
import org.springframework.stereotype.Component;

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
                new ManagerDto(product.getManager().getId(),
                        product.getManager().getFirstName(),
                        product.getManager().getLastName(),product.getManager().getStatus(), null, null), null);
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        throw new UnsupportedOperationException();
    }
}
