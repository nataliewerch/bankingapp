package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.entity.Manager;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ManagerDtoConverter implements Converter<Manager, ManagerDto> {

    private final ClientDtoConverter clientDtoConverter;
    private final ProductDtoConverter productDtoConverter;

    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(),
                manager.getFirstName(),
                manager.getLastName(), manager.getStatus(), null, null);
    }

    @Override
    public Manager toEntity(ManagerDto managerDto) {
        return new Manager(managerDto.getId(),
                managerDto.getFirstName(),
                managerDto.getLastName(),
                managerDto.getStatus(),
                null, null,
                managerDto.getProducts().stream()
                        .map(productDtoConverter::toEntity)
                        .collect(Collectors.toList()),
                managerDto.getClients().stream()
                        .map(clientDtoConverter::toEntity)
                        .collect(Collectors.toList()));
    }
}
