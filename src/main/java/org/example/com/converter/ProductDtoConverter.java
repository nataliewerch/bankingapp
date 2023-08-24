package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Product;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProductDtoConverter implements Converter<Product, ProductDto> {

    private ManagerDtoConverter managerDtoConverter;
    private AgreementDtoConverter agreementDtoConverter;

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
                        product.getManager().getLastName(), product.getManager().getStatus(), null, null), null);
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        return new Product(productDto.getId(),
                productDto.getName(),
                productDto.getStatus(),
                productDto.getCurrencyCode(),
                productDto.getInterestRate(),
                productDto.getLimit(),
                null, null,
                managerDtoConverter.toEntity(productDto.getManager()),
                productDto.getAgreements().stream()
                        .map(agreementDtoConverter::toEntity)
                        .collect(Collectors.toList()));
    }
}
