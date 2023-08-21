package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.service.AccountService;
import org.example.com.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final Converter<Account, AccountDto> converter;
    private final Converter<Transaction, TransactionDto> transactionConverter;

    @GetMapping
    List<AccountDto> getAll() {
        return accountService.getAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    AccountDto getById(@PathVariable(name = "id") UUID id) {
        return converter.toDto(accountService.getById(id));
    }

    @GetMapping("/balance/{id}")
    Double getBalanceByAccountId(@PathVariable(name = "id") UUID id) {
        return accountService.balance(id);
    }

    @GetMapping("/transactions/{id}")
    List<TransactionDto> transactionsHistory(@PathVariable(name = "id") UUID id) {
        return accountService.getTransactionHistory(id).stream()
                .map(transactionConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(converter.toDto(accountService.create(converter.toEntity(accountDto))));
    }

    @PostMapping("/{id}/deposit")
    ResponseEntity<AccountDto> depositIntoTheAccount(@PathVariable(name = "id") UUID id, @RequestParam Double amount, @RequestParam String description) {
        return ResponseEntity.ok(converter.toDto(accountService.deposit(id, amount, description)));
    }

    @PostMapping("/{id}/withdraw")
    ResponseEntity<AccountDto> depositFromTheAccount(@PathVariable(name = "id") UUID id, @RequestParam Double amount, @RequestParam String description) {
        return ResponseEntity.ok(converter.toDto(accountService.withdraw(id, amount, description)));
    }

    @PostMapping("/transfer/{senderId}/{receiverId}")
    ResponseEntity<AccountDto> transfer(@PathVariable(name = "senderId") UUID senderId, @PathVariable(name = "receiverId") UUID receiverId, @RequestParam Double amount, @RequestParam String description) {
        return ResponseEntity.ok(converter.toDto(accountService.transfer(senderId, receiverId, amount, description)));
    }


    @DeleteMapping("/delete")
    void deleteAccount(@RequestBody Account account) {
        accountService.delete(account);
    }

    @DeleteMapping("/delete/{id}")
    void deleteAccountById(@PathVariable(name = "id") UUID id) {
        accountService.deleteById(id);
    }
}
