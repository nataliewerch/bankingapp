package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.AgreementStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Data Transfer Object (DTO) representing an agreement.
 *
 * @author Natalie Werch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDto {

    private Long id;

    @Schema(description = "The interest rate of the agreement", defaultValue = "0.01")
    private BigDecimal interestRate;

    @Schema(description = "The status of the agreement", defaultValue = "ACTIVE")
    private AgreementStatus status;

    @Schema(description = "The sum associated with the agreement", defaultValue = "100.0")
    private Double sum;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private AccountDto accountDto;
    private ProductDto productDto;
}
