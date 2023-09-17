package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.converter.Converter;
import org.example.com.dto.AccountDto;
import org.example.com.entity.Account;
import org.example.com.entity.ClientProfile;
import org.example.com.exception.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the ProfileAccessService interface for managing profile access-related operations.
 *
 * @author Natalie Werch
 */
@Service
@RequiredArgsConstructor
public class ProfileAccessServiceImpl implements ProfileAccessService {

    private final ClientProfileService clientProfileService;
    private final AccountService accountService;
    private final Converter<Account, AccountDto> accountDtoConverter;

    /**
     * Checks if the current user has a client role.
     *
     * @return true if the current user has a client role, otherwise false.
     */
    @Override
    public boolean isClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("CLIENT"));
    }

    /**
     * Checks if the specified account belongs to the specified client.
     *
     * @param accountDto - The AccountDto object representing the account to be checked.
     * @param clientId   - The unique identifier of the client.
     * @return true if the account belongs to the specified client, otherwise false.
     */
    @Override
    public boolean isAccountBelongsToClient(AccountDto accountDto, UUID clientId) {
        return accountDto.getClientDto().getId().equals(clientId);
    }

    /**
     * Filters a list of AccountDto objects to include only those belonging to the current client.
     *
     * @param accountDtos - A list of AccountDto objects to be filtered.
     * @return A filtered list containing only AccountDto objects belonging to the current client.
     */
    @Override
    public List<AccountDto> filterAccountsForClient(List<AccountDto> accountDtos) {
        return accountService.getByClientId(getCurrentClientId()).stream()
                .map(accountDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the unique identifier of the current client.
     *
     * @return The UUID representing the current client.
     */
    @Override
    public UUID getCurrentClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ClientProfile clientProfile = clientProfileService.getByLogin(user.getUsername());
        return clientProfile.getClient().getId();
    }

    /**
     * Checks if the current user has access to the specified account.
     *
     * @param accountId - The unique identifier of the account to be checked for access.
     * @throws AccessDeniedException If the current user does not have access to the specified account.
     */
    @Override
    public void checkAccessToAccount(UUID accountId) {
        if (isClient()) {
            if (!isAccountBelongsToClient(accountDtoConverter.toDto(accountService.getById(accountId)), getCurrentClientId())) {
                throw new AccessDeniedException(String.format("Access denied to account with ID %s", accountId));
            }
        }
    }
}
