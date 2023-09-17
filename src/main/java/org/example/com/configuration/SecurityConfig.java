package org.example.com.configuration;

import lombok.RequiredArgsConstructor;
import org.example.com.service.CustomUserDetailService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for security settings. This class extends the WebSecurityConfigurerAdapter
 * and provides configuration for authentication and authorization in the application.
 */
@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailService customUserDetailService;

    /**
     * Configures HTTP security settings for the application. It disables CSRF protection,
     * defines URL patterns and their corresponding authorization rules, and specifies HTTP basic authentication.
     *
     * @param http - The HttpSecurity object to configure.
     * @throws Exception If an error occurs during security configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                //.antMatchers("/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/accounts", "/accounts/{id}", "/accounts/balance/{id}", "/transactions/**").hasAnyAuthority("CLIENT", "MANAGER")
                .antMatchers(HttpMethod.POST, "/transactions/**").hasAnyAuthority("CLIENT", "MANAGER")
                .antMatchers("/**").hasAuthority("MANAGER")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().disable();
    }

    /**
     * Configures authentication manager to use the custom user details service.
     *
     * @param auth - The AuthenticationManagerBuilder instance.
     * @throws Exception If an error occurs during authentication configuration.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService);
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
