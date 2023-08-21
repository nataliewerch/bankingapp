package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.Agreement;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    private Long id;
    private String name;
    private ProductStatus status;
    private CurrencyCode currencyCode;
    private BigDecimal interestRate;
    private Integer limit;
    private ManagerDto manager;
    private List<Agreement> agreements = new ArrayList<>();
}
