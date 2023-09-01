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


    @Override
    public List<Manager> getAll() {
        List<Manager> managers = managerRepository.findAll();
        if (managers.isEmpty()) {
            throw new ManagerNotFoundException("No managers found");
        }
        return managers;
    }

    @Override
    public Manager getById(Long id) {
        return managerRepository.findById(id)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id %d not found", id)));
    }

    @Override
    public ManagerDto getWithClients(Long id) {
        ManagerDto managerDto = managerDtoConverter.toDto(getById(id));
        List<ClientDto> clientDtos = clientRepository.getAllByManager_Id(id).stream()
                .map(client -> new ClientDto(
                        client.getFirstName(),
                        client.getLastName(),
                        client.getStatus()))
                .collect(Collectors.toList());
        managerDto.setClients(clientDtos);
        return managerDto;
    }

    @Override
    public ManagerDto getWithProducts(Long id) {
        ManagerDto managerDto = managerDtoConverter.toDto(getById(id));
        List<ProductDto> productDtos = productRepository.getAllByManager_Id(id).stream()
                .map(product -> new ProductDto(
                        product.getName(),
                        product.getStatus(),
                        product.getCurrencyCode(),
                        product.getInterestRate(),
                        product.getLimit()))
                .collect(Collectors.toList());
        managerDto.setProducts(productDtos);
        return managerDto;
    }

    @Override
    public Manager create(Manager manager) {
        return managerRepository.save(manager);
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
        List<Client> clientsToReassign = clientRepository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = getById(targetManagerId);

        if (clientsToReassign != null && !clientsToReassign.isEmpty()) {
            for (Client client : clientsToReassign) {
                client.setManager(targetManager);
                clientRepository.save(client);
            }
        } else {
            throw new ClientNotFoundException("No clients found");
        }
    }

    @Override
    public void reassignProducts(Long sourceManagerId, Long targetManagerId) {
        List<Product> productsToReassign = productRepository.getAllByManager_Id(sourceManagerId);
        Manager targetManager = getById(targetManagerId);

        if (productsToReassign != null && !productsToReassign.isEmpty()) {
            for (Product product : productsToReassign) {
                product.setManager(targetManager);
                productRepository.save(product);
            }
        } else {
            throw new ProductNotFoundException(String.format("No products found"));
        }
    }
}
