package org.example.com.service;

import org.example.com.entity.Manager;
import org.example.com.entity.ManagerProfile;
import org.example.com.entity.enums.ManagerStatus;
import org.example.com.exception.LoginAlreadyExistsException;
import org.example.com.exception.ManagerNotFoundException;
import org.example.com.repository.ManagerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ManagerProfileServiceImplTest {

    @Mock
    private ManagerProfileRepository managerProfileRepository;

    @InjectMocks
    private ManagerProfileServiceImpl managerProfileService;

    private List<ManagerProfile> managerProfiles;

    @BeforeEach
    void init() {
        List<Manager> managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, null, null, null));

        managerProfiles = Arrays.asList(
                new ManagerProfile("test", "1111", managers.get(0).getId()),
                new ManagerProfile("test1", "2222", managers.get(1).getId()),
                new ManagerProfile("test2", "3333", managers.get(2).getId()));
    }

    @Test
    void create() {
        Mockito.when(managerProfileRepository.save(managerProfiles.get(0))).thenReturn(managerProfiles.get(0));
        assertEquals(managerProfiles.get(0), managerProfileService.create(managerProfiles.get(0)));
    }

    @Test
    void createLoginAlreadyExists() {
        Mockito.when(managerProfileRepository.existsByLogin(managerProfiles.get(0).getLogin())).thenReturn(true);
        assertThrows(LoginAlreadyExistsException.class, () -> managerProfileService.create(managerProfiles.get(0)));
    }

    @Test
    void getByLogin() {
        Mockito.when(managerProfileRepository.findByLogin(managerProfiles.get(0).getLogin())).thenReturn(managerProfiles.get(0));
        assertEquals(managerProfiles.get(0), managerProfileService.getByLogin(managerProfiles.get(0).getLogin()));
    }

    @Test
    void getByLoginNotFound() {
        Mockito.when(managerProfileRepository.findByLogin(managerProfiles.get(0).getLogin())).thenReturn(null);
        assertThrows(ManagerNotFoundException.class, () -> managerProfileService.getByLogin(managerProfiles.get(0).getLogin()));
    }
}