package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class ClientDtoConverter implements Converter<Client, ClientDto> {

    private final ManagerDtoConverter managerDtoConverter;

    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getStatus(), client.getTaxCode(), client.getFirstName(), client.getLastName(),
                client.getEmail(), client.getPhone(),
                managerDtoConverter.toDto(client.getManager()),
                client.getAccounts().stream()
                        .map(account -> new AccountDto(account.getId(), account.getName(), account.getType(), account.getStatus(), account.getBalance(), account.getCurrencyCode()))
                        .collect(Collectors.toList()));
    }

    @Override
    public Client toEntity(ClientDto clientDto) {
        throw new UnsupportedOperationException();
    }
}
