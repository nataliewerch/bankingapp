package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ClientDtoConverter implements Converter<Client, ClientDto> {
    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(),
                client.getStatus(),
                client.getTaxCode(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(), client.getAddress(),
                client.getPhone(),
                null, null, null);
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
                null, null, null,
//                new Manager(clientDto.getManager().getId(),
//                        clientDto.getManager().getFirstName(),
//                        clientDto.getManager().getLastName(),
//                        clientDto.getManager().getStatus(),
//                        null, null, null, null),
                null);
    }
}
