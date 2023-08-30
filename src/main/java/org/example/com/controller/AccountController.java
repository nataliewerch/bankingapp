package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.*;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.service.AccountService;
import org.example.com.service.AgreementService;
import org.example.com.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final AgreementService agreementService;
    private final ProductService productService;

    @GetMapping
    List<AccountDto> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/{id}")
    AccountDto getById(@PathVariable(name = "id") UUID id) {
        return accountService.getById(id);
    }

    @GetMapping("/status/{accountStatus}")
    List<AccountDto> getAllByStatus(@PathVariable(name = "accountStatus") AccountStatus accountStatus) {
        return accountService.getByStatus(accountStatus);
    }

    @GetMapping("/{clientId}")
    List<AccountDto> getAllByClientId(@PathVariable(name = "clientId") UUID clientId) {
        return accountService.getByClientId(clientId);
    }

    @GetMapping("/balance/{id}")
    Double getBalanceByAccountId(@PathVariable(name = "id") UUID id) {
        return accountService.balance(id);
    }

    @PostMapping("/create/{clientId}")
    AccountDto createAccount(
            @RequestBody AccountDto accountDto,
            @PathVariable(name = "clientId") UUID clientId) {
        return accountService.create(accountDto, clientId);
    }

    @PostMapping("/deposit/{id}/{amount}/{description}")
    void depositIntoTheAccount1(@PathVariable(name = "id") UUID id,
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

    @GetMapping("/transactions/{id}")
    List<TransactionDto> transactionsHistory(@PathVariable(name = "id") UUID id) {
        return accountService.getTransactionHistory(id);
    }

    @DeleteMapping("/delete/{accountId}")
    void deleteAccount(@PathVariable(name = "accountId") UUID accountId) {
        accountService.deleteById(accountId);
    }

    @PostMapping("/agreements/create/{accountId}/{productId}")
    AgreementDto createAgreement(@RequestBody AgreementDto agreementDto,
                                 @PathVariable(name = "accountId") UUID accountId,
                                 @PathVariable(name = "productId") Long productId) {
        return agreementService.create(agreementDto, accountId, productId);
    }

    @GetMapping("/products")
    List<ProductDto> getAllProduct() {
        return productService.getAll();
    }

    @PostMapping("/products/create/{managerId}")
    ProductDto productDto(@RequestBody ProductDto productDto,
                          @PathVariable(name = "managerId") Long managerId) {
        return productService.create(productDto, managerId);
    }
}



