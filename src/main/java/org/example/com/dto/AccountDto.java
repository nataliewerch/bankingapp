package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private UUID id;
    private String name;
    private AccountType type;
    private AccountStatus status;
    private Double balance;
    private CurrencyCode currencyCode;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private ClientDto clientDto;
    private AgreementDto agreementDto;
    private List<TransactionDto> transactions = new ArrayList<>();

    public AccountDto(String name, AccountType type, AccountStatus status, Double balance, CurrencyCode currencyCode) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.balance = balance;
        this.currencyCode = currencyCode;
    }
}
