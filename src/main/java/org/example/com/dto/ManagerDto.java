package org.example.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.com.entity.enums.ManagerStatus;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private ManagerStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private List<ClientDto> clients = new ArrayList<>();
    private List<ProductDto> products = new ArrayList<>();
}
