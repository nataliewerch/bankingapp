package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.TransactionType;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {

    private UUID id;
    private Double amount;
    private TransactionType type;
    private String description;
    private Timestamp createdAt;
    private AccountDto accountDebit;
    private AccountDto accountCredit;
}
