package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.AccountType;
import org.example.com.entity.enums.CurrencyCode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing an account.
 *
 * @author Natalie Werch
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    @Hidden
    private UUID id;

    @Schema(description = "The name of the account", defaultValue = "Saving Account")
    private String name;

    @Schema(description = "The type of the account", defaultValue = "DEPOSIT")
    private AccountType type;

    @Schema(description = "The status of the account", defaultValue = "ACTIVE")
    private AccountStatus status;

    @Schema(description = "The balance of the account", defaultValue = "Saving Account")
    private Double balance;

    @Schema(description = "The currency code of the account", defaultValue = "EUR")
    private CurrencyCode currencyCode;

    @Hidden
    private Timestamp createdAt;

    @Hidden
    private Timestamp updatedAt;

    @Hidden
    private ClientDto clientDto;

    @Hidden
    private AgreementDto agreementDto;

    @Hidden
    private List<TransactionDto> transactions = new ArrayList<>();

    public AccountDto(String name, AccountType type, AccountStatus status, Double balance, CurrencyCode currencyCode, Timestamp createdAt, Timestamp updatedAt, ClientDto clientDto) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.currencyCode = currencyCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.clientDto = clientDto;
    }

    public AccountDto(String name, AccountType type, AccountStatus status, Double balance, CurrencyCode currencyCode) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.currencyCode = currencyCode;
    }
}
