package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.ClientDto;
import org.example.com.dto.ManagerDto;
import org.example.com.dto.ProductDto;
import org.example.com.entity.Client;
import org.example.com.entity.Manager;
import org.example.com.entity.Product;
import org.example.com.exception.*;
import org.example.com.repository.ClientRepository;
import org.example.com.repository.ManagerRepository;
import org.example.com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final Converter<Manager, ManagerDto> managerDtoConverter;
    private final Converter<Client, ClientDto> clientDtoConverter;
    private final Converter<Product, ProductDto> productDtoConverter;


    @Override
    public List<ManagerDto> getAll() {
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) {
            throw new ManagerNotFoundException("No managers found");
        }
        return managers.stream()
                .map(managerDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ManagerDto getById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));
        return managerDtoConverter.toDto(manager);
    }

    @Override
    public ManagerDto getWithClients(Long id) {
        ManagerDto managerDto = getById(id);
        List<ClientDto> clientDtos = clientRepository.getAllByManager_Id(id).stream()
                .map(clientDtoConverter::toDto)
                .collect(Collectors.toList());
        managerDto.setClients(clientDtos);
        return managerDto;
    }

    @Override
    public ManagerDto getWithProducts(Long id) {
        ManagerDto managerDto = getById(id);
        List<ProductDto> products = productRepository.getAllByManager_Id(id).stream()
                .map(productDtoConverter::toDto)
                .collect(Collectors.toList());
        managerDto.setProducts(products);
        return managerDto;
    }

    @Override
    public ManagerDto create(ManagerDto managerDto) {
        return managerDtoConverter.toDto(managerRepository.save(managerDtoConverter.toEntity(managerDto)));
    }

    @Override
    public void deleteById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));

        if (!manager.getClients().isEmpty()) {
            throw new ManagerHasClientsException(String.format("Manager with id: %d  has assigned clients. Reassign clients before deleting the manager!!!", id));
        }
        if (!manager.getProducts().isEmpty()) {
            throw new ManagerHasProductsException(String.format("Manager with id: %d  has assigned products. Reassign products before deleting the manager!!!", id));
        }
        managerRepository.deleteById(id);
    }

    @Override
    public void reassignClients(Long sourceManagerId, Long targetManagerId) {
        ManagerDto sourceManagerDto = getWithClients(sourceManagerId);
        ManagerDto targetManagerDto = getById(targetManagerId);
        Manager targetManager = managerDtoConverter.toEntity(targetManagerDto);

        List<ClientDto> clientsToReassign = sourceManagerDto.getClients();

        if (clientsToReassign != null && !clientsToReassign.isEmpty()) {
            for (ClientDto clientDto : clientsToReassign) {
                clientDto.setManager(targetManagerDto);

                Client client = clientDtoConverter.toEntity(clientDto);
                client.setManager(targetManager);
                clientRepository.save(client);
            }
        } else {
            throw new ClientNotFoundException(String.format("No clients found"));
        }
    }

    @Override
    public void reassignProducts(Long sourceManagerId, Long targetManagerId) {
        ManagerDto sourceManagerDto = getWithProducts(sourceManagerId);
        ManagerDto targetManagerDto = getById(targetManagerId);
        Manager targetManager = managerDtoConverter.toEntity(targetManagerDto);

        List<ProductDto> productsToReassign = sourceManagerDto.getProducts();

        if (productsToReassign != null && !productsToReassign.isEmpty()) {
            for (ProductDto productDto : productsToReassign) {
                productDto.setManager(targetManagerDto);

                Product product = productDtoConverter.toEntity(productDto);
                product.setManager(targetManager);
                productRepository.save(product);
            }
        } else {
            throw new ProductNotFoundException(String.format("No products found"));
        }
    }
}
