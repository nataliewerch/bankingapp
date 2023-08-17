package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Transaction;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TransactionDtoConverter implements Converter<Transaction, TransactionDto> {

    @Override
    public TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(transaction.getAmount(), transaction.getType(), transaction.getDescription(), transaction.getCreatedAt(),
                null);
    }

    @Override
    public Transaction toEntity(TransactionDto transactionDto) {
        throw new UnsupportedOperationException();
    }
}
