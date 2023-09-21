package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
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

    @Hidden
    private Long id;

    @Schema(description = "The interest rate of the agreement", defaultValue = "0.01")
    private BigDecimal interestRate;

    @Schema(description = "The status of the agreement", defaultValue = "ACTIVE")
    private AgreementStatus status;

    @Schema(description = "The sum associated with the agreement", defaultValue = "100.0")
    private Double sum;

    @Hidden
    private Timestamp createdAt;

    @Hidden
    private Timestamp updatedAt;

    @Schema(description = "Account DTO", defaultValue = "{\"id\": \"f842cc6c-dad1-4c4a-a635-7951cbb60d6a\"}")
    private AccountDto accountDto;

    @Schema(description = "Product DTO", defaultValue = "{\"id\": \"5\"}")
    private ProductDto productDto;

    public AgreementDto(Long id, BigDecimal interestRate, AgreementStatus status, Double sum) {
        this.id = id;
        this.interestRate = interestRate;
        this.status = status;
        this.sum = sum;
    }
}
