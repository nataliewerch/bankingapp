package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.ProductStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a product.
 *
 * @author Natalie Werch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    @Hidden
    private Long id;

    @Schema(description = "The name of the product", defaultValue = "Saving Account")
    private String name;

    @Schema(description = "The status of the product", defaultValue = "ACTIVE")
    private ProductStatus status;

    @Schema(description = "The currency code of the product", defaultValue = "EUR")
    private CurrencyCode currencyCode;

    @Schema(description = "The interest rate of the product", defaultValue = "0.01")
    private BigDecimal interestRate;

    @Schema(description = "The limit of the product", defaultValue = "1000")
    private Integer limit;

    @Hidden
    private Timestamp createdAt;

    @Hidden
    private Timestamp updatedAt;

    @Hidden
    private ManagerDto manager;

    @Hidden
    private List<AgreementDto> agreements = new ArrayList<>();

    public ProductDto(String name, ProductStatus status, CurrencyCode currencyCode, BigDecimal interestRate, Integer limit) {
        this.name = name;
        this.status = status;
        this.currencyCode = currencyCode;
        this.interestRate = interestRate;
        this.limit = limit;
    }

    public ProductDto(Long id, String name, ProductStatus status, CurrencyCode currencyCode, BigDecimal interestRate, Integer limit) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.currencyCode = currencyCode;
        this.interestRate = interestRate;
        this.limit = limit;
    }
}
