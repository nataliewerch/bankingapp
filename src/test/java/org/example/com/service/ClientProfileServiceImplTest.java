package org.example.com.service;

import org.example.com.entity.Client;
import org.example.com.entity.ClientProfile;
import org.example.com.entity.Manager;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.entity.enums.ManagerStatus;
import org.example.com.exception.LoginAlreadyExistsException;
import org.example.com.repository.ClientProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ClientProfileServiceImplTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @InjectMocks
    private ClientProfileServiceImpl clientProfileService;

    private List<ClientProfile> clientProfile;

    @BeforeEach
    void init() {
        List<Manager> managers = Arrays.asList(
                new Manager(1L, "Anton", "Antonov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(2L, "Ivan", "Ivanov", ManagerStatus.ACTIVE, null, null, null, null, null),
                new Manager(3L, "Anna", "Stock", ManagerStatus.ACTIVE, null, null, null, null, null));

        List<Client> clients = Arrays.asList(
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "123456789", "Inna", "Scheff", "scheff@gmail", "32144 Bonn, Hoffmanstrasse 12", "+49 157 5454 6632", null, null, managers.get(0), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "987654321", "Artur", "Petrov", "petrov@gmail", "32132 Bonn, Alterstrasse 77", "+49 178 7732 1654", null, null, managers.get(1), null, null),
                new Client(UUID.randomUUID(), ClientStatus.ACTIVE, "112233445", "Stepan", "Stepanov", "stepanov@gmail", "32144 Bonn, Krommelstrasse 12", "+49 179 4567 3321", null, null, managers.get(2), null, null));

        clientProfile = Arrays.asList(
                new ClientProfile("test", "1111", clients.get(0).getId()),
                new ClientProfile("test1", "2222", clients.get(1).getId()),
                new ClientProfile("test2", "3333", clients.get(2).getId()));
    }

    @Test
    void create() {
        Mockito.when(clientProfileRepository.save(clientProfile.get(0))).thenReturn(clientProfile.get(0));
        assertEquals(clientProfile.get(0), clientProfileService.create(clientProfile.get(0)));
    }

    @Test
    void createLoginAlreadyExists() {
        Mockito.when(clientProfileRepository.existsByLogin(clientProfile.get(0).getLogin())).thenReturn(true);
        assertThrows(LoginAlreadyExistsException.class, () -> clientProfileService.create(clientProfile.get(0)));
    }

    @Test
    void getByLogin() {
        Mockito.when(clientProfileRepository.findByLogin(clientProfile.get(0).getLogin())).thenReturn(clientProfile.get(0));
        assertEquals(clientProfile.get(0), clientProfileService.getByLogin(clientProfile.get(0).getLogin()));
    }
}