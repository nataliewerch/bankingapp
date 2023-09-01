package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Transaction;
import org.example.com.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final AccountService accountService;
    private final Converter<Transaction, TransactionDto> transactionDtoConverter;

    @GetMapping("/{accountId}")
    List<TransactionDto> transactionsHistory(@PathVariable(name = "accountId") UUID accountId) {
        return accountService.getTransactionHistory(accountId).stream()
                .map(transactionDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/deposit/{id}/{amount}/{description}")
    void depositIntoTheAccount(@PathVariable(name = "id") UUID id,
                               @PathVariable(name = "amount") Double amount,
                               @PathVariable(name = "description") String description) {
        accountService.deposit(id, amount, description);
    }

    @PostMapping("/withdraw/{id}/{amount}/{description}")
    void depositFromTheAccount(@PathVariable(name = "id") UUID id,
                               @PathVariable(name = "amount") Double amount,
                               @PathVariable(name = "description") String description) {
        accountService.withdraw(id, amount, description);
    }

    @PostMapping("/transfer/{senderId}/{receiverId}/{amount}/{description}")
    void transfer(@PathVariable(name = "senderId") UUID senderId,
                  @PathVariable(name = "receiverId") UUID receiverId,
                  @PathVariable(name = "amount") Double amount,
                  @PathVariable(name = "description") String description) {
        accountService.transfer(senderId, receiverId, amount, description);
    }
}
