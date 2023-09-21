package org.example.com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.com.converter.Converter;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.ClientDto;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Agreement;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.entity.enums.*;
import org.example.com.exception.AgreementNotFoundException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc(addFilters = false)
class ManagerControllerTest {

    @MockBean
    private ManagerService managerService;

    @MockBean
    private ProductService productService;

    @MockBean
    private AgreementService agreementService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ManagerProfileService managerProfileService;

    @MockBean
    private ClientProfileService clientProfileService;

    @MockBean
    private Converter<Manager, ManagerDto> managerDtoConverter;

    @MockBean
    private Converter<Product, ProductDto> productDtoConverter;

    @MockBean
    private Converter<Agreement, AgreementDto> agreementDtoConverter;
    private static final Logger logger = LoggerFactory.getLogger(ManagerControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    private ProductDto productDto;
    private Manager manager;

    private ManagerDto managerDto;

    List<ClientDto> clientDtos;
    private Agreement agreement;
    private AgreementDto agreementDto;

    @BeforeEach
    void init() {
        manager = new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null);
        Client client = new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "123456789", "Inna", "Scheff", "scheff@gmail", "32144 Bonn, Hoffmanstrasse 12", "+49 157 5454 6632", null, null, manager, null, null);
        clientDtos = List.of(new ClientDto(client.getFirstName(), client.getLastName(), client.getStatus()));
        managerDto = new ManagerDto(manager.getFirstName(), manager.getLastName(), manager.getStatus());
        product = new Product(1L, "Savings Accounts", ProductStatus.ACTIVE, CurrencyCode.USD, new BigDecimal("0.00"), 1000, null, null, manager, null);
        productDto = new ProductDto(product.getId(), product.getName(), product.getStatus(), product.getCurrencyCode(), product.getInterestRate(), product.getLimit());
        agreement = new Agreement(1L, new BigDecimal("0.00"), AgreementStatus.ACTIVE, 1000.0, null, null, null, null);
        agreementDto = new AgreementDto(agreement.getId(), agreement.getInterestRate(), agreement.getStatus(), agreement.getSum());
    }


    @Test
    void getAll() throws Exception {
        Mockito.when(managerService.getAll()).thenReturn(List.of(manager));
        Mockito.when(managerDtoConverter.toDto(manager)).thenReturn(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(managerDto))));
    }

    @Test
    void getById() throws Exception {
        Mockito.when(managerService.getById(manager.getId())).thenReturn(manager);
        Mockito.when(managerDtoConverter.toDto(manager)).thenReturn(managerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/" + manager.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(managerDto)));
    }

    @Test
    void getByIdWhenIdIsNotExists() throws Exception {
        Mockito.when(managerService.getById(manager.getId())).thenThrow(ManagerNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/{id}", manager.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerNotFoundException));
    }

    @Test
    void deleteManagerByID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/{Id}", manager.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    void deleteManagerByIdWhenManagerNotFound() throws Exception {
        Mockito.doThrow(ManagerNotFoundException.class)
                .when(managerService)
                .deleteById(manager.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/{Id}", manager.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ManagerNotFoundException));
    }

    @Test
    void reassignClients() throws Exception {
        Manager targetManager = new Manager(2L, "Anna", "Antonova", ManagerStatus.ACTIVE, null, null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/managers/reassign-clients/{sourceManagerId}/{targetManagerId}", manager.getId(), targetManager.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(clientService).reassignClients(manager.getId(), targetManager.getId());
    }

    @Test
    void reassignProducts() throws Exception {
        Manager targetManager = new Manager(2L, "Anna", "Antonova", ManagerStatus.ACTIVE, null, null, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/managers/reassign-products/{sourceManagerId}/{targetManagerId}", manager.getId(), targetManager.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(productService).reassignProducts(manager.getId(), targetManager.getId());
    }

    @Test
    void createProduct() throws Exception {
        Long managerId = 1L;
        ProductDto newProductDto = new ProductDto(null, "Savings Accounts", ProductStatus.ACTIVE, CurrencyCode.USD, new BigDecimal("0.00"), 1000);
        Product newProductWithoutId = new Product(null, newProductDto.getName(), newProductDto.getStatus(), newProductDto.getCurrencyCode(), newProductDto.getInterestRate(), newProductDto.getLimit(), null, null, null, null);
        Product newProduct = new Product(10L, newProductWithoutId.getName(), newProductWithoutId.getStatus(), newProductWithoutId.getCurrencyCode(), newProductWithoutId.getInterestRate(), newProductWithoutId.getLimit(), null, null, null, null);

        Mockito.when(productDtoConverter.toEntity(newProductDto)).thenReturn(newProductWithoutId);
        Mockito.when(productService.create(newProductWithoutId, managerId)).thenReturn(newProduct);
        Mockito.when(productDtoConverter.toDto(newProduct)).thenReturn(
                new ProductDto(newProduct.getId(), newProduct.getName(), newProduct.getStatus(), newProduct.getCurrencyCode(), newProduct.getInterestRate(), newProduct.getLimit()));

        mockMvc.perform(MockMvcRequestBuilders.post("/managers//{managerId}/products", managerId)
                        .content(asJsonString(newProductDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getAllProducts() throws Exception {
        Mockito.when(productService.getAll()).thenReturn(List.of(product));
        Mockito.when(productDtoConverter.toDto(product)).thenReturn(productDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/products/").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(productDto))));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/products/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void deleteProductWhenProductNotFound() throws Exception {
        Mockito.doThrow(ProductNotFoundException.class)
                .when(productService)
                .deleteById(product.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/managers/products/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProductNotFoundException));
    }

    @Test
    void getAllAgreements() throws Exception {
        Mockito.when(agreementService.getAll()).thenReturn(List.of(agreement));
        Mockito.when(agreementDtoConverter.toDto(agreement)).thenReturn(agreementDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/agreements/").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(asJsonString(List.of(agreementDto))));
    }


    @Test
    void createAgreement() throws Exception {
        Long productId = 1L;
        UUID accountId = UUID.randomUUID();
        AgreementDto newAgreementDto = new AgreementDto(null, new BigDecimal("0.03"), AgreementStatus.ACTIVE, 100.0, null, null, null, null);
        Agreement newAgreementWithoutId = new Agreement(null, newAgreementDto.getInterestRate(), newAgreementDto.getStatus(), newAgreementDto.getSum(), null, null, null, null);
        Agreement newAgreement = new Agreement(10L, newAgreementWithoutId.getInterestRate(), newAgreementWithoutId.getStatus(), newAgreementWithoutId.getSum(), null, null, null, null);

        Mockito.when(agreementDtoConverter.toEntity(newAgreementDto)).thenReturn(newAgreementWithoutId);
        Mockito.when(agreementService.create(newAgreementWithoutId, accountId, productId)).thenReturn(newAgreement);
        Mockito.when(agreementDtoConverter.toDto(newAgreement)).thenReturn(
                new AgreementDto(newAgreement.getId(), newAgreement.getInterestRate(), newAgreement.getStatus(), newAgreement.getSum(), null, null, null, null));

        mockMvc.perform(MockMvcRequestBuilders.post("/managers/agreements")
                        .content(asJsonString(newAgreementDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void deleteAgreementById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/managers//agreements/{agreementId}", agreement.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(agreementService).deleteById(agreement.getId());
    }

    @Test
    void deleteAgreementWhenAgreementNotFound() throws Exception {
        Mockito.doThrow(AgreementNotFoundException.class)
                .when(agreementService)
                .deleteById(agreement.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/managers//agreements/{agreementId}", agreement.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AgreementNotFoundException));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
