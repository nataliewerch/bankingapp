package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.ManagerProfile;
import org.example.com.entity.enums.ManagerStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a manager.
 *
 * @author Natalie Werch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {

    @Hidden
    private Long id;

    @Schema(description = "The first name of the manager", defaultValue = "Anna")
    private String firstName;

    @Schema(description = "The last name of the manager", defaultValue = "Klose")
    private String lastName;

    @Schema(description = "The status of the manager", defaultValue = "ACTIVE")
    private ManagerStatus status;

    @Hidden
    private Timestamp createdAt;

    @Hidden
    private Timestamp updatedAt;

    @Schema(description = "Manager Profile",
            defaultValue = "{\"login\": \"manager\"," +
                    " \"password\": " +
                    "\"manager\"}")
    private ManagerProfile managerProfile;

    @Hidden
    private List<ClientDto> clients = new ArrayList<>();

    @Hidden
    private List<ProductDto> products = new ArrayList<>();

    public ManagerDto(String firstName, String lastName, ManagerStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }
}
