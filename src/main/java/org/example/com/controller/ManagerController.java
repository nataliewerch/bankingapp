package org.example.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AgreementDto;
import org.example.com.dto.ClientDto;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.*;
import org.example.com.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for managing managers.
 *
 * @author Natalie Werch
 */
@Tag(name = "Manager Controller", description = "Controller for managing managers")
@RequiredArgsConstructor
@RestController
@RequestMapping("managers")
public class ManagerController {

    private final ManagerService managerService;
    private final ProductService productService;
    private final AgreementService agreementService;
    private final ClientService clientService;
    private final Converter<Manager, ManagerDto> managerDtoConverter;
    private final Converter<Product, ProductDto> productDtoConverter;
    private final Converter<Agreement, AgreementDto> agreementDtoConverter;
    private final Converter<Client, ClientDto> clientDtoConverter;

    /**
     * Get a list of all managers.
     *
     * @return List of ManagerDto objects representing managers.
     */
    @Operation(
            summary = "Get a list of all managers",
            description = "Allows you to get a list of all managers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "No managers found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping
    public List<ManagerDto> getAll() {
        return managerService.getAll().stream()
                .map(managerDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get a manager by their identifier.
     *
     * @param id The unique identifier of the manager.
     * @return ManagerDto representing the manager.
     */
    @Operation(
            summary = "Get a manager by their identifier",
            description = "Allows you to get a manager by their identifier ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/{id}")
    public ManagerDto getById(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the manager") Long id) {
        return managerDtoConverter.toDto(managerService.getById(id));
    }

    /**
     * Get a manager with their clients by manager identifier.
     *
     * @param managerId The unique identifier of the manager.
     * @return ManagerDto representing the manager along with their clients.
     */
    @Operation(
            summary = "Get a manager with their clients by manager identifier",
            description = "Allows you to retrieve a manager along with their clients by manager identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager or client not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/clients/{managerId}")
    public ManagerDto getManagerWithClients(@PathVariable(name = "managerId") @Parameter(description = "The unique identifier of the manager") Long managerId) {
        ManagerDto managerDto = managerDtoConverter.toDto(managerService.getById(managerId));
        List<ClientDto> clientDtos = clientService.getAllByManagerId(managerId).stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
        managerDto.setClients(clientDtos);
        return managerDto;
    }

    /**
     * Get a manager with their products by manager identifier.
     *
     * @param managerId The unique identifier of the manager.
     * @return ManagerDto representing the manager along with their products.
     */
    @Operation(
            summary = "Get a manager with their products by manager identifier",
            description = "Allows you to retrieve a manager along with their products by manager identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager or product not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/products/{managerId}")
    public ManagerDto getManagerWithProducts(@PathVariable(name = "managerId") @Parameter(description = "The unique identifier of the manager") Long managerId) {
        ManagerDto managerDto = managerDtoConverter.toDto(managerService.getById(managerId));
        List<ProductDto> productDtos = productService.getAllByManagerId(managerId).stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
        managerDto.setProducts(productDtos);
        return managerDto;
    }

    /**
     * Create a manager with their profile.
     *
     * @param managerDto The manager information, including their profile, to create.
     * @return ManagerDto representing the created manager.
     */
    @Operation(
            summary = "Create a manager with their profile",
            description = "Allows a manager to create a manager along with their profile",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully request"),
                    @ApiResponse(responseCode = "409", description = "A manager with the same login already exists!")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManagerDto createManager(@RequestBody @Parameter(description = "The manager information, including their profile, to create") ManagerDto managerDto) {
        String login = managerDto.getManagerProfile().getLogin();
        String password = managerDto.getManagerProfile().getPassword();
        return managerDtoConverter.toDto(managerService.create(managerDtoConverter.toEntity(managerDto), login, password));
    }

    /**
     * Delete a manager by its identifier.
     *
     * @param id The unique identifier of the manager to delete.
     */
    @Operation(
            summary = "Delete a manager by its identifier",
            description = "Allows you to delete a manager by its identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManagerById(@PathVariable(name = "id") @Parameter(description = "The unique identifier of the manager") Long id) {
        managerService.deleteById(id);
    }

    /**
     * Reassign clients from source manager to target manager.
     *
     * @param sourceManagerId The ID of the source manager whose clients will be reassigned.
     * @param targetManagerId The ID of the target manager to whom the clients will be reassigned.
     */
    @Operation(
            summary = "Reassign clients from source manager to target manager",
            description = "This endpoint allows reassigning clients from one manager to another",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager or client not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/reassign-clients/{sourceManagerId}/{targetManagerId}")
    public void reassignClients(@PathVariable(name = "sourceManagerId") @Parameter(description = "The ID of the source manager whose clients will be reassigned") Long sourceManagerId,
                                @PathVariable(name = "targetManagerId") @Parameter(description = "The ID of the target manager to whom the clients will be reassigned") Long targetManagerId) {
        clientService.reassignClients(sourceManagerId, targetManagerId);
    }

    /**
     * Reassign products from source manager to target manager.
     *
     * @param sourceManagerId The ID of the source manager whose products will be reassigned.
     * @param targetManagerId The ID of the target manager to whom the products will be reassigned.
     */
    @Operation(
            summary = "Reassign products from source manager to target manager",
            description = "This endpoint allows reassigning products from one manager to another",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager or product not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/reassign-products/{sourceManagerId}/{targetManagerId}")
    public void reassignProducts(@PathVariable(name = "sourceManagerId") @Parameter(description = "The ID of the source manager whose products will be reassigned") Long sourceManagerId,
                                 @PathVariable(name = "targetManagerId") @Parameter(description = "The ID of the target manager to whom the products will be reassigned") Long targetManagerId) {
        productService.reassignProducts(sourceManagerId, targetManagerId);
    }

    /**
     * Create a product by a manager.
     *
     * @param productDto The product information to create.
     * @param managerId  The unique identifier of the manager creating the product.
     * @return ProductDto representing the created product.
     */
    @Operation(
            summary = "Create a product by a manager",
            description = "Allows a manager to create a product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/{managerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody @Parameter(description = "The product information to create") ProductDto productDto,
                                    @PathVariable(name = "managerId") @Parameter(description = "The unique identifier of the manager") Long managerId) {
        return productDtoConverter.toDto(productService.create(productDtoConverter.toEntity(productDto), managerId));
    }

    /**
     * Get a list of all products.
     *
     * @return List of ProductDto objects representing products.
     */
    @Operation(
            summary = "Get a list of all products",
            description = "Allows you to get a list of all products",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "No products found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/products")
    public List<ProductDto> getAllProducts() {
        return productService.getAll().stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Delete a product by its identifier.
     *
     * @param productId The unique identifier of the product to delete.
     */
    @Operation(
            summary = "Delete a product by its identifier",
            description = "Allows you to delete a product by its identifier",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable(name = "productId") @Parameter(description = "The unique identifier of the product") Long productId) {
        productService.deleteById(productId);
    }

    /**
     * Get a list of all agreements.
     *
     * @return List of AgreementDto objects representing agreements.
     */
    @Operation(
            summary = "Get a list of all agreements",
            description = "Allows you to get a list of all agreements",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "No agreements found")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @GetMapping("/agreements")
    public List<AgreementDto> getAllAgreements() {
        return agreementService.getAll().stream()
                .map(agreementDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create an agreement.
     *
     * @param agreementDto The agreement information to create.
     * @return AgreementDto representing the created agreement.
     */
    @Operation(
            summary = "Create an agreement",
            description = "Create a new agreement for a specific account and product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully request"),
                    @ApiResponse(responseCode = "404", description = "Manager not found"),
                    @ApiResponse(responseCode = "409", description = "Agreement for account is already exist")
            }
    )
    @SecurityRequirement(name = "basicauth")
    @PostMapping("/agreements")
    @ResponseStatus(HttpStatus.CREATED)
    public AgreementDto createAgreement(@RequestBody @Parameter(description = "The agreement information to create") AgreementDto agreementDto) {
        UUID accountId = (agreementDto.getAccountDto() != null) ? agreementDto.getAccountDto().getId() : null;
        Long productId = (agreementDto.getProductDto() != null) ? agreementDto.getProductDto().getId() : null;
        return agreementDtoConverter.toDto(agreementService.create(agreementDtoConverter.toEntity(agreementDto), accountId, productId));
    }
}
