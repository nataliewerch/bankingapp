package org.example.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.entity.Account;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.service.AccountService;
import org.example.com.service.ProfileAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing accounts.
 *
 * @author Natalie Werch
 */
@Tag(name = "Account Controller", description = "Controller for managing accounts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final Converter<Account, AccountDto> accountDtoConverter;
    private final ProfileAccessService profileAccessService;

    /**
     * Retrieves a list of all accounts with their owner's names.
     * For managers, this method returns all accounts of all clients.
     * For clients, this method returns only their own accounts.
     *
     * @return A list of AccountDto representing all accounts.
     */
    @Operation(
            summary = "Get a list of all accounts",
            description = "Allows you to get a list of all accounts with the names of their owners",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "No accounts found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    List<AccountDto> getAll() {
        List<AccountDto> accountDtos = accountService.getAll().stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());

        if (profileAccessService.isClient()) {
            return profileAccessService.filterAccountsForClient(accountDtos);
        }
        return accountDtos;
    }

    /**
     * Retrieves an account by its identifier.
     *
     * @param id - The unique identifier of the account.
     * @return An AccountDto representing the account.
     */
    @Operation(
            summary = "Get an account by its identifier",
            description = "Allows you to get an account with the owner's name by its identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    AccountDto getById(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the account") UUID id) {
        profileAccessService.checkAccessToAccount(id);
        return accountDtoConverter.toDto(accountService.getById(id));
    }

    /**
     * Retrieves a list of all accounts by status.
     *
     * @param accountStatus - The status of the account to filter by.
     * @return A list of AccountDto representing accounts with the specified status.
     */
    @Operation(
            summary = "Get a list of all accounts by status",
            description = "Allows you to get a list of all accounts with the names of their owners by their status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Accounts not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/status/{accountStatus}")
    List<AccountDto> getAllByStatus(@PathVariable(name = "accountStatus") @Parameter(description = "The status of the account to filter by") AccountStatus accountStatus) {
        return accountService.getByStatus(accountStatus).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all accounts by client identifier.
     *
     * @param clientId - The unique identifier of the client.
     * @return A list of AccountDto representing accounts belonging to the specified client.
     */
    @Operation(
            summary = "Get a list of all accounts by client identifier",
            description = "Allows you to retrieve a list of all accounts by client identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/clients/{clientId}")
    List<AccountDto> getAllByClientId(@PathVariable(name = "clientId") @Parameter(description = "The unique identifier of the client") UUID clientId) {
        return accountService.getByClientId(clientId).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the balance of an account by its identifier.
     *
     * @param id - The unique identifier of the account.
     * @return The account balance.
     */
    @Operation(
            summary = "Get an account balance by its identifier",
            description = "Allows you to retrieve the balance of an account by its identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/balance/{id}")
    Double getBalanceByAccountId(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the account") UUID id) {
        profileAccessService.checkAccessToAccount(id);
        return accountService.balance(id);
    }

    /**
     * Creates a new account for the specified client.
     *
     * @param accountDto - The account information to create.
     * @param clientId   - The unique identifier of the client.
     * @return The created AccountDto representing the newly created account.
     */
    @Operation(
            summary = "Create a new account for the specified client",
            description = "Allows you to create a new account for the specified client",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    AccountDto createAccount(
            @RequestBody @Parameter(description = "The account information to create") AccountDto accountDto,
            @PathVariable(name = "clientId") @Parameter(description = "The unique identifier of the client") UUID clientId) {
        return accountDtoConverter.toDto(accountService.create(accountDtoConverter.toEntity(accountDto), clientId));
    }

    /**
     * Deletes an account by its identifier.
     *
     * @param accountId - The unique identifier of the account to delete.
     */
    @Operation(
            summary = "Delete an account by its identifier",
            description = "Allows you to delete an account by its identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAccount(@PathVariable(name = "accountId") @Parameter(description = "The unique identifier of the account") UUID accountId) {
        accountService.deleteById(accountId);
    }
}



