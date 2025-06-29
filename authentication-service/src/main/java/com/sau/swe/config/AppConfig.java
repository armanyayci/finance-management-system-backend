package com.sau.swe.config;

import com.sau.swe.dao.UserRepository;
import com.sau.swe.security.UserImpl;
import com.sau.swe.utils.exception.GenericFinanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository usersRepository;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new GenericFinanceException("generic.auth.userNotFound"));

            UserImpl userImpl = new UserImpl();
            userImpl.setId(user.getId());
            userImpl.setUsername(user.getUsername());
            userImpl.setPassword(user.getPassword());
            userImpl.setEmail(user.getEmail());
            userImpl.setFirstName(user.getFirstName());
            userImpl.setLastName(user.getLastName());
            userImpl.setStatus(user.getStatus());
            userImpl.setCreatedAt(user.getCreatedAt());
            userImpl.setUpdatedAt(user.getUpdatedAt());
            userImpl.setAccounts(user.getAccounts());
            userImpl.setRoles(user.getRoles());
            
            return userImpl;
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
