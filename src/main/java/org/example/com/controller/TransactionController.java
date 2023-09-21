package org.example.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.TransactionDto;
import org.example.com.dto.TransactionRequestDto;
import org.example.com.entity.Transaction;
import org.example.com.exception.AccessDeniedException;
import org.example.com.service.AccountService;
import org.example.com.service.ProfileAccessService;
import org.example.com.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing transactions.
 *
 * @author Natalie Werch
 */
@Tag(name = "TransactionController", description = "Controller for managing transactions")
@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Converter<Transaction, TransactionDto> transactionDtoConverter;
    private final ProfileAccessService profileAccessService;

    /**
     * Get a list of all transactions by account identifier.
     *
     * @param accountId - The unique identifier of the account.
     * @return List of TransactionDto representing transaction history.
     */
    @Operation(
            summary = "Get a list of all transactions by account identifier",
            description = "Allows you to get a list of all transactions by account identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "No accounts or transactions found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{accountId}")
    List<TransactionDto> transactionsHistory(@PathVariable(name = "accountId") @Parameter(description = "The unique identifier of the account") UUID accountId) {
        profileAccessService.checkAccessToAccount(accountId);
        return accountService.getTransactionHistory(accountId).stream()
                .map(transactionDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deposit into the account.
     *
     * @param transactionRequestDto The transaction request DTO containing accountId, amount, and description.
     */
    @Operation(
            summary = "Deposit into the account",
            description = "Deposits a specified amount into the user's account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "No account found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/deposit")
    void depositIntoTheAccount(@RequestBody @Parameter(description = "The transaction request details") TransactionRequestDto transactionRequestDto) {
        profileAccessService.checkAccessToAccount(transactionRequestDto.getSenderId());
        accountService.deposit(transactionRequestDto.getSenderId(), transactionRequestDto.getAmount(), transactionRequestDto.getDescription());
    }

    /**
     * Withdraw from the account.
     *
     * @param transactionRequestDto The transaction request DTO containing accountId, amount, and description.
     */
    @Operation(
            summary = "Withdraw from the account",
            description = "Withdraws a specified amount from the user's account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "403", description = "Insufficient funds in the account"),
                    @ApiResponse(responseCode = "404", description = "No account found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/withdraw")
    void withdraw(@RequestBody @Parameter(description = "The transaction request details") TransactionRequestDto transactionRequestDto) {
        profileAccessService.checkAccessToAccount(transactionRequestDto.getSenderId());
        accountService.withdraw(transactionRequestDto.getSenderId(), transactionRequestDto.getAmount(), transactionRequestDto.getDescription());
    }

    /**
     * Transfer funds between accounts.
     *
     * @param transactionRequestDto The transaction request DTO containing senderId, receiverId, amount, and description.
     */
    @Operation(
            summary = "Transfer funds between accounts",
            description = "Transfers a specified amount from the sender's account to the receiver's account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "403", description = "Insufficient funds in the sender's account"),
                    @ApiResponse(responseCode = "404", description = "One or both accounts not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/transfer")
    void transfer(@RequestBody @Parameter(description = "The transaction request details") TransactionRequestDto transactionRequestDto) {
        profileAccessService.checkAccessToAccount(transactionRequestDto.getSenderId());
        accountService.transfer(transactionRequestDto.getSenderId(), transactionRequestDto.getReceiverId(), transactionRequestDto.getAmount(), transactionRequestDto.getDescription());
    }

    /**
     * Delete a transaction by its identifier.
     *
     * @param id - The unique identifier of the transaction.
     */
    @Operation(
            summary = "Delete a transaction by its identifier",
            description = "Allows you to delete a transaction by its identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Transaction not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTransactionById(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the transaction") UUID id) {
        if (profileAccessService.isClient()) {
            throw new AccessDeniedException("Access denied. Only managers can delete transactions.");
        }
        transactionService.delete(id);
    }
}