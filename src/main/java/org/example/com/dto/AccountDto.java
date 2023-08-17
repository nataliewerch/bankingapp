package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private UUID id;
    private String name;
    private Double balance;
    // private ClientDto client;
    private UUID clientId;
    private List<TransactionDto> transactions;

}
