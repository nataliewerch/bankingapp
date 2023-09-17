package org.example.com.service;

import lombok.RequiredArgsConstructor;
import org.example.com.entity.ClientProfile;
import org.example.com.entity.ManagerProfile;
import org.example.com.repository.ClientProfileRepository;
import org.example.com.repository.ManagerProfileRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom implementation of Spring Security's UserDetailsService for loading user details based on the username.
 *
 * @author Natalie Werch
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final ClientProfileRepository clientProfileRepository;
    private final ManagerProfileRepository managerProfileRepository;

    /**
     * Load user details by username, either for a manager or a client.
     *
     * @param username - The username for which user details are to be loaded.
     * @return A UserDetails object representing the user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    /**
     * Get user details by username, checking if it belongs to a manager or a client.
     *
     * @param username - The username for which user details are to be retrieved.
     * @throws UsernameNotFoundException If the user with the specified username is not found.
     */
    private User getUserByUsername(String username) {
        ManagerProfile managerProfile = managerProfileRepository.findByLogin(username);
        if (managerProfile != null) {
            return new User(managerProfile.getLogin(), managerProfile.getPassword(), List.of(new SimpleGrantedAuthority("MANAGER")));
        }

        ClientProfile clientProfile = clientProfileRepository.findByLogin(username);
        if (clientProfile != null) {
            return new User(clientProfile.getLogin(), clientProfile.getPassword(), List.of(new SimpleGrantedAuthority("CLIENT")));
        }
        throw new UsernameNotFoundException(String.format("User with username %s not found", username));
    }
}
