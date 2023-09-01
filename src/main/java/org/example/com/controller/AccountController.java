package org.example.com.controller;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.*;
import org.example.com.entity.Account;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;
    private final Converter<Account, AccountDto> accountDtoConverter;

    @GetMapping
    List<AccountDto> getAll() {
        return accountService.getAll().stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/accountId/{id}")
    AccountDto getById(@PathVariable(name = "id") UUID id) {
        return accountDtoConverter.toDto(accountService.getById(id));
    }

    @GetMapping("/status/{accountStatus}")
    List<AccountDto> getAllByStatus(@PathVariable(name = "accountStatus") AccountStatus accountStatus) {
        return accountService.getByStatus(accountStatus).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients/{clientId}")
    List<AccountDto> getAllByClientId(@PathVariable(name = "clientId") UUID clientId) {
        return accountService.getByClientId(clientId).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/balance/{id}")
    Double getBalanceByAccountId(@PathVariable(name = "id") UUID id) {
        return accountService.balance(id);
    }

    @PostMapping("/create/{clientId}")
    AccountDto createAccount(
            @RequestBody AccountDto accountDto,
            @PathVariable(name = "clientId") UUID clientId) {
        return accountDtoConverter.toDto(accountService.create(accountDtoConverter.toEntity(accountDto), clientId));
    }

    @DeleteMapping("/delete/{accountId}")
    void deleteAccount(@PathVariable(name = "accountId") UUID accountId) {
        accountService.deleteById(accountId);
    }
}



