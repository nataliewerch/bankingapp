package org.example.com.service;

import org.example.com.entity.ClientProfile;
import org.example.com.entity.ManagerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private ManagerProfileService managerProfileService;

    @Mock
    private ClientProfileService clientProfileService;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    private ManagerProfile managerProfile;
    private ClientProfile clientProfile;

    @BeforeEach
    void init() {
        managerProfile = new ManagerProfile("manager", "manager1", 1L);
        clientProfile = new ClientProfile("client", "client1", UUID.randomUUID());
    }

    @Test
    void loadUserByUsernameForManager() {
        Mockito.when(managerProfileService.getByLogin(managerProfile.getLogin())).thenReturn(managerProfile);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(managerProfile.getLogin());
        assertNotNull(userDetails);
        assertEquals(managerProfile.getLogin(), userDetails.getUsername());
        assertEquals(managerProfile.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameForClient() {
        Mockito.when(clientProfileService.getByLogin(clientProfile.getLogin())).thenReturn(clientProfile);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(clientProfile.getLogin());
        assertNotNull(userDetails);
        assertEquals(clientProfile.getLogin(), userDetails.getUsername());
        assertEquals(clientProfile.getPassword(), userDetails.getPassword());
    }
}