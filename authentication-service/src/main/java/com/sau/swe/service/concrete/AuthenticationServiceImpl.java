package com.sau.swe.service.concrete;

import com.sau.swe.dao.RoleRepository;
import com.sau.swe.dao.UserRepository;
import com.sau.swe.dto.CreateAccountDTO;
import com.sau.swe.dto.LoginDto;
import com.sau.swe.dto.SignUpDto;
import com.sau.swe.dto.TokenResponse;
import com.sau.swe.entity.Roles;
import com.sau.swe.entity.Users;
import com.sau.swe.security.JwtService;
import com.sau.swe.security.UserImpl;
import com.sau.swe.service.Abstract.AuthenticationService;

import com.sau.swe.utils.Constants;
import com.sau.swe.utils.exception.GenericFinanceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository usersRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final RestTemplate template;
    private final PasswordEncoder passwordEncoder;
    
    private UserImpl convertToUserImpl(Users user) {
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
    }

    @Override
    public TokenResponse login(LoginDto loginDto) {
        try {
            Users user = usersRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(()-> new NullPointerException(String.format("username not found : %s",loginDto.getUsername())));
            UserImpl userImpl = convertToUserImpl(user);
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

            String token = jwtService.generateToken(userImpl);
            return TokenResponse.builder().token(token).build();
        }
        catch (AuthenticationException e){
            log.info("Password is not correct.");
            throw e;
        }
    }

    @Override
    public void signup(SignUpDto sign) {
        Users user=Users.builder()
                .username(sign.getUsername())
                .password(passwordEncoder.encode(sign.getPassword()))
                .email(sign.getEmail())
                .firstName(sign.getFirstname())
                .lastName(sign.getLastname())
                .createdAt(LocalDateTime.now())
                .build();
        Roles userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new GenericFinanceException("generic.auth.signIn"));
        user.setRoles(List.of(userRole));
        usersRepository.save(user);
        CreateAccountDTO accountDTO = CreateAccountDTO.builder()
                .userId(usersRepository.findByUsername(user.getUsername()).get().getId())
                .accountType("TRY")
                .build();
        String url = Constants.CREATE_ACCOUNT_URL;
        try {
            String x = template.postForObject(url,accountDTO, String.class);
            System.out.println(x);
        }
         catch (ResourceAccessException e) {
            log.error("Failed to request to the URL: " + url);
            throw new GenericFinanceException(e.getMessage());
        }
    }
}
