package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.Agreement;
import org.example.com.entity.ClientProfile;
import org.example.com.entity.enums.ClientStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a client.
 *
 * @author Natalie Werch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    private UUID id;

    @Schema(description = "The status of the client", defaultValue = "ACTIVE")
    private ClientStatus status;

    @Schema(description = "The tax code of the client", defaultValue = "1111111111")
    private String taxCode;

    @Schema(description = "The first name of the client", defaultValue = "Ivan")
    private String firstName;

    @Schema(description = "The last name of the client", defaultValue = "Ivanov")
    private String lastName;

    @Schema(description = "The email address of the client", defaultValue = "ivanov@example.com")
    private String email;

    @Schema(description = "The address of the client", defaultValue = "10115 Berlin Schwarzstraße 23")
    private String address;

    @Schema(description = "The phone number of the client", defaultValue = "+49 179 3445 7788")
    private String phone;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    private ManagerDto manager;
    private List<AccountDto> accounts;
    private List<Agreement> agreements;
    private ClientProfile clientProfile;

    public ClientDto(String firstName, String lastName, ClientStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public ClientDto(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public ClientDto(UUID id, ClientStatus status, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
