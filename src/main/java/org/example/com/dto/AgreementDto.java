package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.AgreementStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDto {

    private Long id;
    private BigDecimal interestRate;
    private AgreementStatus status;
    private Double sum;
    private AccountDto account;
    private ProductDto product;
}
