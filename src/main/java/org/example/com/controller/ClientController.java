package org.example.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.dto.ClientDto;
import org.example.com.entity.Client;
import org.example.com.entity.ClientProfile;
import org.example.com.entity.enums.ClientStatus;
import org.example.com.exception.LoginAlreadyExistsException;
import org.example.com.service.ClientProfileService;
import org.example.com.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing clients.
 */
@Tag(name = "Client Controller", description = "Controller for managing clients")
@RequiredArgsConstructor
@RestController
@RequestMapping("clients")
public class ClientController {

    private final ClientService clientService;
    private final Converter<Client, ClientDto> clientDtoConverter;
    private final ClientProfileService clientProfileService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves a list of all clients.
     *
     * @return A list of ClientDto representing all clients.
     */
    @Operation(
            summary = "Get a list of all clients",
            description = "Allows you to get a list of all clients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "No clients found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll().stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a client by their identifier.
     *
     * @param clientId - The unique identifier of the client.
     * @return The ClientDto representing the retrieved client.
     */
    @Operation(
            summary = "Get a client by their identifier",
            description = "Allows you to get a client by their identifier ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{clientId}")
    public ClientDto getById(@PathVariable(name = "clientId") @Parameter(description = "The unique identifier of the client") UUID clientId) {
        return clientDtoConverter.toDto(clientService.getById(clientId));
    }

    /**
     * Retrieves a list of all clients by their status.
     *
     * @param status - The status of the clients to filter by.
     * @return A list of ClientDto representing clients with the specified status.
     */
    @Operation(
            summary = "Get a list of all clients by status",
            description = "Allows you to get a list of all clients by their status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Clients not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/status/{status}")
    public List<ClientDto> getAllByStatus(@PathVariable(name = "status") @Parameter(description = "The status of the client to filter by") ClientStatus status) {
        return clientService.getAllByStatus(status).stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a client along with their accounts by client identifier.
     *
     * @param clientId - The unique identifier of the client.
     * @return The ClientDto representing the client along with their accounts.
     */
    @Operation(
            summary = "Get a client with their accounts by client identifier",
            description = "Allows you to retrieve a client along with their accounts by client identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Clients not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/accounts/{clientId}")
    public ClientDto getClientWithAccounts(@PathVariable(name = "clientId") @Parameter(description = "The unique identifier of the client") UUID clientId) {
        Client client = clientService.getById(clientId);
        ClientDto clientDto = clientDtoConverter.toDto(client);
        List<AccountDto> accountDtos = client.getAccounts().stream()
                .map(account -> new AccountDto(
                        account.getName(),
                        account.getType(),
                        account.getStatus(),
                        account.getBalance(),
                        account.getCurrencyCode()))
                .toList();
        clientDto.setAccounts(accountDtos);
        return clientDto;
    }

    /**
     * Retrieves the account balance by client and account identifier.
     *
     * @param clientId  - The unique identifier of the client.
     * @param accountId - The unique identifier of the account.
     * @return The account balance as a Double value.
     */
    @Operation(
            summary = "Get account balance by client and account identifier",
            description = "Allows you to retrieve the account balance by client and account identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/balance/{clientId}/{accountId}")
    public Double getBalanceByClientIdAndAccountId(@PathVariable(name = "clientId") @Parameter(description = "The unique identifier of the client") UUID clientId,
                                                   @PathVariable(name = "accountId") @Parameter(description = "The unique identifier of the account") UUID accountId) {
        return clientService.balance(clientId, accountId);
    }

    /**
     * Creates a client along with their profile by a manager.
     *
     * @param clientDto - The client information, including their profile, to create.
     * @param managerId - The unique identifier of the manager.
     * @return The created ClientDto representing the newly created client.
     */
    @Operation(
            summary = "Create a client with their profile by a manager",
            description = "Allows a manager to create a client along with their profile",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Manager not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "409", description = "A client with the same login already exists!"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/{managerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto createClient(@RequestBody @Parameter(description = "The client information, including their profile, to create") ClientDto clientDto,
                                  @PathVariable(name = "managerId") @Parameter(description = "The unique identifier of the manager") Long managerId) {
        String login = clientDto.getClientProfile().getLogin();
        if (clientProfileService.existsByLogin(login)) {
            throw new LoginAlreadyExistsException(String.format("Client with login %s already exists!", login));
        }
        Client client = clientService.create(clientDtoConverter.toEntity(clientDto), managerId);
        clientProfileService.create(
                new ClientProfile(login, passwordEncoder.encode(clientDto.getClientProfile().getPassword()), client.getId()));
        return clientDtoConverter.toDto(clientService.create(client, managerId));
    }

    /**
     * Changes the status of a client.
     *
     * @param id        - The unique identifier of the client.
     * @param newStatus - The new status to set for the client.
     * @return The updated ClientDto representing the client with the new status.
     */
    @Operation(
            summary = "Change client status",
            description = "Allows you to change the status of a client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/change-status/{id}/{newStatus}")
    public ClientDto changeStatus(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the client") UUID id,
                                  @PathVariable(name = "newStatus") @Parameter(description = "The new status to set for the client") ClientStatus newStatus) {
        return clientDtoConverter.toDto(clientService.changeStatus(id, newStatus));
    }

    /**
     * Deletes a client by its identifier.
     *
     * @param id - The unique identifier of the client to delete.
     */
    @Operation(
            summary = "Delete a client by its identifier",
            description = "Allows you to delete a client by its identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully request"),
                    @ApiResponse(responseCode = "400", description = "Bad Request. The request contains invalid data or has an incorrect format"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Access denied"),
                    @ApiResponse(responseCode = "404", description = "Client not found"),
                    @ApiResponse(responseCode = "405", description = "Method Not Allowed"),
                    @ApiResponse(responseCode = "500", description = "Internal error")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClientById(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the client") UUID id) {
        clientService.deleteById(id);
    }
}
