package org.example.com.converter;

import lombok.RequiredArgsConstructor;
import org.example.com.dto.ManagerDto;
import org.example.com.entity.Manager;
import org.springframework.stereotype.Component;

/**
 * A converter class for converting between Manager entities and ManagerDto DTOs.
 * This class provides methods for converting a manager entity to its corresponding DTO and back.
 *
 * @author Natalie Werch
 */
@RequiredArgsConstructor
@Component
public class ManagerDtoConverter implements Converter<Manager, ManagerDto> {

    /**
     * Converts a Manager entity to a ManagerDto DTO.
     *
     * @param manager - The Manager entity to convert.
     * @return The corresponding ManagerDto DTO.
     */
    @Override
    public ManagerDto toDto(Manager manager) {
        return new ManagerDto(manager.getFirstName(),
                manager.getLastName(),
                manager.getStatus());
    }

    /**
     * Converts a ManagerDto DTO to a Manager entity.
     *
     * @param managerDto - The ManagerDto DTO to convert.
     * @return The corresponding Manager entity.
     */
    @Override
    public Manager toEntity(ManagerDto managerDto) {
        return new Manager(managerDto.getId(),
                managerDto.getFirstName(),
                managerDto.getLastName(),
                managerDto.getStatus(), null,
                null, null, null, null);
    }
}
