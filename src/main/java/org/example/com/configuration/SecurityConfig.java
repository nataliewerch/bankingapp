package org.example.com.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security settings. This class extends the WebSecurityConfigurerAdapter
 * and provides configuration for authentication and authorization in the application.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * Configures HTTP security settings for the application. It disables CSRF protection,
     * defines URL patterns and their corresponding authorization rules, and specifies HTTP basic authentication.
     *
     * @param http The HttpSecurity object to configure.
     * @throws Exception If an error occurs during security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts", "/accounts/{id}", "/accounts/balance/{id}", "/transactions/**").hasAnyAuthority("CLIENT", "MANAGER")
                .antMatchers(HttpMethod.POST, "/transactions/**").hasAnyAuthority("CLIENT", "MANAGER")
                .antMatchers("/**").hasAuthority("MANAGER")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable().build();
    }

    /**
     * Provides a PasswordEncoder bean for encoding and verifying passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
