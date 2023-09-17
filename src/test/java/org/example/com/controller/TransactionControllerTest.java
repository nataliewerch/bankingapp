package org.example.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.com.converter.Converter;
import org.example.com.dto.TransactionDto;
import org.example.com.entity.Account;
import org.example.com.entity.Transaction;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.AccountType;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.TransactionType;
import org.example.com.exception.TransactionNotFoundException;
import org.example.com.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private Converter<Transaction, TransactionDto> transactionDtoConverter;

    @MockBean
    private ProfileAccessService profileAccessService;

    @MockBean
    private ClientProfileService clientProfileService;

    @MockBean
    private ManagerProfileService managerProfileService;

    @MockBean
    private AgreementService agreementService;

    @Autowired
    private MockMvc mockMvc;

    private Transaction transaction;
    private Account account;

    private TransactionDto transactionDto;


    @BeforeEach
    void init() {
        account = new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, new Timestamp(System.currentTimeMillis()), null, null, null, null, null);

        transaction = new Transaction(UUID.randomUUID(), TransactionType.DEPOSIT, 200.0, "Deposit to Account", null, account, null);
        transactionDto = new TransactionDto(transaction.getId(), transaction.getAmount(), transaction.getType(), transaction.getDescription(), null, null, null);
    }

    @Test
    void transactionsHistory() throws Exception {
        Mockito.when(accountService.getTransactionHistory(account.getId())).thenReturn(List.of(transaction));
        Mockito.when(transactionDtoConverter.toDto(transaction)).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/{accountId}", account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(transactionDto))));

        Mockito.verify(accountService).getTransactionHistory(account.getId());
    }

    @Test
    void depositIntoTheAccount() throws Exception {
        Mockito.when(profileAccessService.isClient()).thenReturn(false);
        Mockito.when(profileAccessService.isClient()).thenReturn(true);
        Mockito.doNothing().when(profileAccessService).checkAccessToAccount(account.getId());
        Mockito.doNothing().when(accountService).deposit(account.getId(), transaction.getAmount(), transaction.getDescription());
        Mockito.when(transactionDtoConverter.toDto(transaction)).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/deposit/{id}/{amount}/{description}", account.getId(), transaction.getAmount(), transaction.getDescription())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).deposit(account.getId(), transaction.getAmount(), transaction.getDescription());
    }


    @Test
    void withdraw() throws Exception {
        Mockito.when(profileAccessService.isClient()).thenReturn(true);
        Mockito.doNothing().when(profileAccessService).checkAccessToAccount(account.getId());
        Mockito.doNothing().when(accountService).withdraw(account.getId(), transaction.getAmount(), transaction.getDescription());
        Mockito.when(transactionDtoConverter.toDto(transaction)).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/withdraw/{id}/{amount}/{description}", account.getId(), transaction.getAmount(), transaction.getDescription())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).withdraw(account.getId(), transaction.getAmount(), transaction.getDescription());
    }

    @Test
    void transfer() throws Exception {
        UUID receiverId = UUID.randomUUID();
        Mockito.when(profileAccessService.isClient()).thenReturn(true);
        Mockito.doNothing().when(profileAccessService).checkAccessToAccount(account.getId());
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/transfer/{senderId}/{receiverId}/{amount}/{description}",
                                account.getId(), receiverId, transaction.getAmount(), transaction.getDescription())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(accountService).transfer(account.getId(), receiverId, transaction.getAmount(), transaction.getDescription());
    }

    @Test
    void deleteTransactionById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/{Id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(transactionService).delete(transaction.getId());
    }

    @Test
    void deleteTransactionByIdWhenTransactionNotFound() throws Exception {
        Mockito.doThrow(TransactionNotFoundException.class)
                .when(transactionService)
                .delete(transaction.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/{Id}", transaction.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof TransactionNotFoundException));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}