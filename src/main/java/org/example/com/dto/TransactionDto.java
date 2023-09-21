package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.TransactionType;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a transaction.
 *
 * @author Natalie Werch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private UUID id;

    @Schema(description = "The amount of the transaction", defaultValue = "100.0")
    private Double amount;

    @Schema(description = "The type of the transaction", defaultValue = "DEPOSIT")
    private TransactionType type;

    @Schema(description = "A description of the transaction", defaultValue = "Withdrawal")
    private String description;

    private Timestamp createdAt;

    private AccountDto accountDebit;
    private AccountDto accountCredit;

    public TransactionDto(Double amount, TransactionType type, String description, Timestamp createdAt) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
    }

    public TransactionDto(UUID id, Double amount, TransactionType type, String description) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
}
