package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.ClientDto;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.*;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.exception.ClientNotFoundException;
import org.example.com.service.AccountService;
import org.example.com.converter.Converter;
import org.example.com.service.AgreementService;
import org.example.com.service.ClientService;
import org.example.com.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final AgreementService agreementService;
    private final ProductService productService;
    private final ClientService clientService;
    private final Converter<Account, AccountDto> converter;
    private final Converter<Client, ClientDto> clientDtoConverter;
    private final Converter<Transaction, TransactionDto> transactionConverter;
    private final Converter<Agreement, AgreementDto> agreementConverter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);

    @GetMapping
    List<AccountDto> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    AccountDto getById(@PathVariable(name = "id") UUID id) {
        return accountService.getById(id);
    }

    @GetMapping("/status/{accountStatus}")
    List<AccountDto> getAllByStatus(@PathVariable(name = "accountStatus")AccountStatus accountStatus) {
        return accountService.getByStatus(accountStatus);
    }
    @GetMapping("/{clientId}")
    List<AccountDto> getAllByClientId(@PathVariable(name = "clientId")UUID clientId) {
        return accountService.getByClientId(clientId);
    }

    @GetMapping("/balance/{id}")
    Double getBalanceByAccountId(@PathVariable(name = "id") UUID id) {
        return accountService.balance(id);
    }

    @PostMapping("/create-for-client/{clientId}")
    AccountDto createAccount(
            @RequestBody AccountDto accountDto,
            @PathVariable(name = "clientId") UUID clientId) {
        ClientDto clientDto = clientDtoConverter.toDto(clientService.getById(clientId));
        return accountService.create(accountDto, clientId);
    }

    @PostMapping("/deposit/{id}/{amount}/{description}")
    AccountDto depositIntoTheAccount(@PathVariable(name = "id") UUID id,
                                     @PathVariable(name = "amount") Double amount,
                                     @PathVariable(name = "description") String description) {
        return accountService.deposit(id, amount, description);
    }

    @PostMapping("/withdraw/{id}/{amount}/{description}")
    AccountDto depositFromTheAccount(@PathVariable(name = "id") UUID id,
                                     @PathVariable(name = "amount") Double amount,
                                     @PathVariable(name = "description") String description) {
        return accountService.withdraw(id, amount, description);
    }

    @PostMapping("/transfer/{senderId}/{receiverId}/{amount}/{description}")
    TransactionDto transfer(@PathVariable(name = "senderId") UUID senderId,
                            @PathVariable(name = "receiverId") UUID receiverId,
                            @PathVariable(name = "amount") Double amount,
                            @PathVariable(name = "description") String description) {
        return accountService.transfer(senderId, receiverId, amount, description);
    }

    @GetMapping("/transactions/{id}")
    List<TransactionDto> transactionsHistory(@PathVariable(name = "id") UUID id) {
        return accountService.getTransactionHistory(id);
    }


    @DeleteMapping("/{accountId}")
    void deleteAccount(@PathVariable(name = "accountId") UUID accountId) {
        accountService.deleteById(accountId);
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



