package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Transaction;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Transaction entities and TransactionDto DTOs.
 * This class provides methods for converting a transaction entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class TransactionDtoConverter implements Converter<Transaction, TransactionDto> {

    /**
     * Converts a Transaction entity to a TransactionDto DTO.
     *
     * @param transaction - The Transaction entity to convert.
     * @return The corresponding TransactionDto DTO.
     */
    @Override
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(null,
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                null, null);
    }

    /**
     * Converts a TransactionDto DTO to a Transaction entity.
     *
     * @param transactionDto - The TransactionDto DTO to convert.
     * @return The corresponding Transaction entity.
     */
    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        return new Transaction(transactionDto.getId(),
                transactionDto.getType(),
                transactionDto.getAmount(),
                transactionDto.getDescription(),
                transactionDto.getCreatedAt(), null, null);
    }
}
