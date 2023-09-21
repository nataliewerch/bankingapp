package org.example.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.entity.Account;
import org.example.com.entity.enums.AccountStatus;
import org.example.com.entity.enums.AccountType;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.exception.AccountNotFoundException;
import org.example.com.service.*;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private Converter<Account, AccountDto> converter;

    @MockBean
    private ClientProfileService clientProfileService;

    @MockBean
    private ManagerProfileService managerProfileService;

    @MockBean
    ProfileAccessService profileAccessService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ManagerService managerService;

    @MockBean
   private ProductService productService;

    @MockBean
    private AgreementService agreementService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    private Account account;
    private AccountDto accountDto;

    @BeforeEach
    void init() {
        account = new Account(UUID.randomUUID(), "Savings Accounts", AccountType.DEPOSIT, AccountStatus.ACTIVE, 3400.0, CurrencyCode.USD, null, null, null, null, null, null);

        accountDto = new AccountDto(account.getName(), account.getType(), account.getStatus(), account.getBalance(), account.getCurrencyCode());
    }


    @Test
    void getAll() throws Exception {
        Mockito.when(accountService.getAll()).thenReturn(List.of(account));
        Mockito.when(converter.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(accountDto))));
    }


    @Test
    void getById() throws Exception {
        Mockito.when(accountService.getById(account.getId())).thenReturn(account);
        Mockito.when(converter.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + account.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(accountDto)));
    }

    @Test
    void getByIdWhenIdIsNotExists() throws Exception {
        Mockito.when(accountService.getById(account.getId())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{id}", account.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException));
    }

    @Test
    void getAllByStatus() throws Exception {
        Mockito.when(accountService.getByStatus(AccountStatus.ACTIVE)).thenReturn(List.of(account));
        Mockito.when(converter.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/status/{accountStatus}", AccountStatus.ACTIVE.toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(accountDto))));
    }

    @Test
    void getAllByStatusWhenAccountNotFound() throws Exception {
        Mockito.when(accountService.getByStatus(account.getStatus())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/status/{accountStatus}", AccountStatus.ACTIVE.toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException));
    }

    @Test
    void getBalanceByAccountId() throws Exception {
        Mockito.when(accountService.balance(account.getId())).thenReturn(account.getBalance());

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/balance/{id}", account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(account.getBalance()));
    }

    @Test
    void getBalanceByAccountIdWhenAccountNotFound() throws Exception {
        Mockito.when(accountService.balance(account.getId())).thenThrow(AccountNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/balance/{id}", account.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException));
    }

    @Test
    void createAccount() throws Exception {
        UUID clientId = UUID.randomUUID();
        AccountDto newAccountDto = new AccountDto("SAVING", AccountType.DEPOSIT, AccountStatus.ACTIVE, 1000.0, CurrencyCode.USD);
        Account newAccountWithoutId = new Account(null, newAccountDto.getName(), newAccountDto.getType(), newAccountDto.getStatus(), newAccountDto.getBalance(), newAccountDto.getCurrencyCode(), null, null, null, null, null, null);
        Account newAccount = new Account(UUID.randomUUID(), newAccountWithoutId.getName(), newAccountWithoutId.getType(), newAccountWithoutId.getStatus(), newAccountWithoutId.getBalance(), newAccountWithoutId.getCurrencyCode(), null, null, null, null, null, null);

        Mockito.when(converter.toEntity(newAccountDto)).thenReturn(newAccountWithoutId);
        Mockito.when(accountService.create(newAccountWithoutId, clientId)).thenReturn(newAccount);
        Mockito.when(converter.toDto(newAccount)).thenReturn(
                new AccountDto(newAccount.getId(), newAccount.getName(), newAccount.getType(), newAccount.getStatus(), newAccount.getBalance(), newAccount.getCurrencyCode(), null, null, null, null, null));

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{clientId}", clientId.toString())
                        .content(asJsonString(newAccountDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(newAccount.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("SAVING")));
    }

    @Test
    void deleteAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{accountId}", account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteAccountWhenAccountNotFound() throws Exception {
        Mockito.doThrow(AccountNotFoundException.class)
                .when(accountService)
                .deleteById(account.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{accountId}", account.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotFoundException));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}