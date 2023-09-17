package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Client entities and ClientDto DTOs.
 * This class provides methods for converting a client entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class ClientDtoConverter implements Converter<Client, ClientDto> {

    /**
     * Converts a Client entity to a ClientDto DTO.
     *
     * @param client - The Client entity to convert.
     * @return The corresponding ClientDto DTO.
     */
    @Override
    public ClientDto toDto(Client client) {
        return new ClientDto(client.getId(),
                client.getStatus(),
                null,
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(), null,
                client.getPhone(), null,
                null, null, null, null, null);
    }

    /**
     * Converts a ClientDto DTO to a Client entity.
     *
     * @param clientDto - The ClientDto DTO to convert.
     * @return The corresponding Client entity.
     */
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
                null, null, null, null, null);
    }
}
