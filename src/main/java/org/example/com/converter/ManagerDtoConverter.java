package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ClientDto;
import org.example.com.dto.ManagerDto;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ManagerDtoConverter implements Converter<Manager, ManagerDto> {

    private final Converter<Client, ClientDto> converter;

    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getStatus(), null, null,
                null, null);
    }


    @Override
    public Manager toEntity(ManagerDto managerDto) {
        return new Manager(managerDto.getId(),
                managerDto.getFirstName(),
                managerDto.getLastName(),
                managerDto.getStatus(),
                null, null, null, null);
    }
}
