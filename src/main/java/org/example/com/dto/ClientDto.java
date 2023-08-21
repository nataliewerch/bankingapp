package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.ClientStatus;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

    private UUID id;
    private ClientStatus status;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private ManagerDto manager;
    private List<AccountDto> accounts;
}
