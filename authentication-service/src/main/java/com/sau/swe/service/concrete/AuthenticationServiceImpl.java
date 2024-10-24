package com.sau.swe.service.concrete;

import com.sau.swe.dto.LoginDto;
import com.sau.swe.dto.SignUpDto;
import com.sau.swe.dto.TokenResponse;
import com.sau.swe.entity.Roles;
import com.sau.swe.entity.Users;
import com.sau.swe.repository.RoleRepository;
import com.sau.swe.repository.UsersRepository;
import com.sau.swe.security.JwtService;
import com.sau.swe.service.Abstract.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsersRepository usersRepository;;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public TokenResponse login(LoginDto loginDto) {
        Users user = usersRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(
                        ()-> new NullPointerException(String.format("username not found : %s",loginDto.getUsername())));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginDto.getUsername(), loginDto.getPassword()));

        List<String> roles = user.getRoles().stream().map(Roles::getName).toList();

        String token = jwtService.generateToken(user,roles);

        return TokenResponse.builder().token(token).build();
    }

    @Override
    public void signup(SignUpDto sign) {
        Users users=Users.builder()
                .username(sign.getUsername())
                .password(sign.getPassword())
                .email(sign.getEmail())
                .firstName(sign.getFirstname())
                .lastName(sign.getLastname())
                .createdAt(LocalDateTime.now())
                .build();

        Roles userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        users.setRoles(List.of(userRole));

    }
}
