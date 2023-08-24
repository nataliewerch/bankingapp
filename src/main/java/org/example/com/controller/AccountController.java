package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.*;
import org.example.com.service.AccountService;
import org.example.com.converter.Converter;
import org.example.com.service.AgreementService;
import org.example.com.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    private final AgreementService agreementService;
    private final ProductService productService;
    private final Converter<Account, AccountDto> converter;
    private final Converter<Transaction, TransactionDto> transactionConverter;
    private final Converter<Agreement, AgreementDto> agreementConverter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

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

    @PostMapping("/deposit/{id}/{amount}/{description}")
    AccountDto depositIntoTheAccount(@PathVariable(name = "id") UUID id, @PathVariable(name = "amount") Double amount, @PathVariable(name = "description") String description) {
        return converter.toDto(accountService.deposit(id, amount, description));
    }

    @PostMapping("/withdraw/{id}/{amount}/{description}")
    ResponseEntity<AccountDto> depositFromTheAccount(@PathVariable(name = "id") UUID id, @PathVariable(name = "amount") Double amount, @PathVariable(name = "description") String description) {
        return ResponseEntity.ok(converter.toDto(accountService.withdraw(id, amount, description)));
    }

    @PostMapping("/transfer/{senderId}/{receiverId}/{amount}/{description}")
    ResponseEntity<TransactionDto> transfer(@PathVariable(name = "senderId") UUID senderId, @PathVariable(name = "receiverId") UUID receiverId, @PathVariable(name = "amount") Double amount, @PathVariable(name = "description") String description) {
        return ResponseEntity.ok(transactionConverter.toDto(accountService.transfer(senderId, receiverId, amount, description)));
    }


    @DeleteMapping("/delete")
    void deleteAccount(@RequestBody Account account) {
        accountService.delete(account);
    }

    @DeleteMapping("/delete/{id}")
    void deleteAccountById(@PathVariable(name = "id") UUID id) {
        agreementService.deleteByAccountId(id);
        accountService.deleteById(id);
    }

    @PostMapping("/agreements/create")
    ResponseEntity<AgreementDto> create(@RequestBody AgreementDto agreementDto) {

        try {
            logger.info("ЗАПРОС ПОЛУЧЕН: {}", agreementDto);

            Agreement agreement = agreementConverter.toEntity(agreementDto);
            Agreement createdAgreement = agreementService.create(agreement);
            AgreementDto createdAgreementDto = agreementConverter.toDto(createdAgreement);


            logger.info("ОПЕРАЦИЯ ПРОШЛА УСПЕШНО: {}", createdAgreement);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdAgreementDto);
        } catch (Exception e) {
            logger.error("ОШИБКААА", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/products")
    List<Product> getAllProduct() {
        return productService.getAll();
    }
}



