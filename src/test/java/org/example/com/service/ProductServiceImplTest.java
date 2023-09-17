package org.example.com.service;

import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.entity.enums.CurrencyCode;
import org.example.com.entity.enums.ManagerStatus;
import org.example.com.entity.enums.ProductStatus;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.exception.ProductNotFoundException;
import org.example.com.repository.ManagerRepository;
import org.example.com.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private List<Product> products;
    private List<Manager> managers;

    @BeforeEach
    void init() {
        managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, null, null, null));

        products = Arrays.asList(
                new Product(1L, "Savings Accounts", ProductStatus.ACTIVE, CurrencyCode.USD, new BigDecimal("0.00"), 1000, null, null, managers.get(0), null),
                new Product(2L, "Credit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.05"), 25000, null, null, managers.get(1), null),
                new Product(1L, "Debit Card", ProductStatus.ACTIVE, CurrencyCode.EUR, new BigDecimal("0.00"), 7000, null, null, managers.get(2), null));
    }

    @Test
    void getAll() {
        Mockito.when(productRepository.findAll()).thenReturn(products);
        assertEquals(3, productService.getAll().size());
    }

    @Test
    void getAllWithNoProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ProductNotFoundException.class, () -> productService.getAll());
    }

    @Test
    void getById() {
        Long productId = products.get(0).getId();
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(products.get(0)));
        Product result = productService.getById(productId);
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals(ProductStatus.ACTIVE, result.getStatus());
        assertEquals(1000, result.getLimit());
    }

    @Test
    void getByIdWhenProductsNotFound() {
        Mockito.when(productRepository.findById(products.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getById(products.get(0).getId()));
    }

    @Test
    void getAllByManagerId() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.of(managers.get(0)));
        Mockito.when(productRepository.getAllByManager_Id(managers.get(0).getId())).thenReturn(products);
        List<Product> productList = productService.getAllByManagerId(managers.get(0).getId());
        assertEquals(products.size(), productList.size());
    }

    @Test
    void getAllByManagerIdWhenManagerNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> productService.getAllByManagerId(managers.get(0).getId()));
    }

    @Test
    void create() {
        Long managerId = managers.get(0).getId();
        Mockito.when(managerRepository.findById(managerId)).thenReturn(Optional.of(managers.get(0)));
        Mockito.when(productRepository.save(products.get(0))).thenReturn(products.get(0));

        Product result = productService.create(products.get(0), managerId);

        assertEquals(products.get(0), result);
        assertEquals(products.get(0).getId(), result.getId());
        assertEquals(products.get(0).getLimit(), result.getLimit());
    }

    @Test
    void createManagerNotFound() {
        Mockito.when(managerRepository.findById(managers.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ManagerNotFoundException.class, () -> productService.create(products.get(0), managers.get(0).getId()));
    }

    @Test
    void deleteById() {
        Mockito.when(productRepository.findById(products.get(0).getId())).thenReturn(Optional.of(products.get(0)));
        productService.deleteById(products.get(0).getId());
        Mockito.verify(productRepository).delete(products.get(0));
    }

    @Test
    void getByLoginNotFound() {
        Mockito.when(productRepository.findById(products.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.getById(products.get(0).getId()));
    }
}