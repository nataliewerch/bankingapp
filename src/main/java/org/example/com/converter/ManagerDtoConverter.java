package org.example.com.converter;

import org.example.com.dto.ManagerDto;
import org.example.com.entity.Manager;
import org.springframework.stereotype.Component;

@Component
public class ManagerDtoConverter implements Converter<Manager, ManagerDto> {

    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getId(),
                manager.getFirstName(),
                manager.getLastName(), manager.getStatus(), null, null);
    }

    @Override
    public Manager toEntity(ManagerDto managerDto) {
        throw new UnsupportedOperationException();
    }
}
