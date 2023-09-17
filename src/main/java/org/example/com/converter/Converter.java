package org.example.com.converter;

/**
 * A generic interface for converting between entity and DTO objects.
 *
 * @param <ENTITY> - The type of the entity object.
 * @param <DTO>    - The type of the DTO (Data Transfer Object) object.
 * @author Natalie Werch
 */
public interface Converter<ENTITY, DTO> {

    /**
     * Converts an entity object to its corresponding DTO.
     *
     * @param entity - The entity object to be converted.
     * @return The corresponding DTO object.
     */
    DTO toDto(ENTITY entity);

    /**
     * Converts a DTO object to its corresponding entity.
     *
     * @param dto - The DTO object to be converted.
     * @return The corresponding entity object.
     */
    ENTITY toEntity(DTO dto);
}
