package org.example.com.service;

import org.example.com.dto.AccountDto;

import java.util.List;
import java.util.UUID;

/**
 * This interface defines the contract for profile access-related operations.
 *
 * @author Natalie Werch
 */
public interface ProfileAccessService {

    boolean isClient();

    boolean isAccountBelongsToClient(AccountDto accountDto, UUID clientId);

    UUID getCurrentClientId();

    void checkAccessToAccount(UUID accountId);

    List<AccountDto> filterAccountsForClient(List<AccountDto> accountDtos);
}
