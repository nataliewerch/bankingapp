package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.dto.ManagerDto;
import org.example.com.entity.Client;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ClientDtoConverter implements Converter<Client, ClientDto> {

    private ManagerDtoConverter managerDtoConverter;
    private AccountDtoConverter accountDtoConverter;

    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(),
                client.getStatus(),
                client.getTaxCode(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone(),
                new ManagerDto(client.getManager().getId(), client.getManager().getFirstName(), client.getManager().getLastName(), null, null, null),
                client.getAccounts().stream()
                        .map(account -> new AccountDto(account.getId(), account.getName(), account.getType(), account.getStatus(), account.getBalance(), account.getCurrencyCode(), null, null))
                        .collect(Collectors.toList()),
                null);
    }

    @Override
    public Client toEntity(ClientDto clientDto) {
        return new Client(clientDto.getId(),
                clientDto.getStatus(),
                clientDto.getTaxCode(),
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getEmail(),
                clientDto.getAddress(),
                clientDto.getPhone(),
                null, null,
                managerDtoConverter.toEntity(clientDto.getManager()),
                clientDto.getAccounts().stream()
                        .map(accountDtoConverter::toEntity)
                        .toList());
    }
}
