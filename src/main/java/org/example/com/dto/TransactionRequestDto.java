package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequestDto {

    private UUID senderId;
    private UUID receiverId;
    private Double amount;
    private String description;

    public TransactionRequestDto(UUID senderId, Double amount, String description) {
        this.senderId = senderId;
        this.amount = amount;
        this.description = description;
    }
}
