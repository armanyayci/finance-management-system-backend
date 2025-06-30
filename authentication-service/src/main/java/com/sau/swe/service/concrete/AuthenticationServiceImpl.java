package com.sau.swe.service.concrete;

import com.sau.swe.config.RabbitMQConfig;
import com.sau.swe.dao.AccountRepository;
import com.sau.swe.dao.RoleRepository;
import com.sau.swe.dao.UserRepository;
import com.sau.swe.dao.VerificationCodeRepository;
import com.sau.swe.dto.*;
import com.sau.swe.entity.Account;
import com.sau.swe.entity.Roles;
import com.sau.swe.entity.Users;
import com.sau.swe.entity.VerificationCode;
import com.sau.swe.security.JwtService;
import com.sau.swe.security.UserImpl;
import com.sau.swe.service.Abstract.AuthenticationService;

import com.sau.swe.utils.Constants;
import com.sau.swe.utils.exception.GenericFinanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
    private final VerificationCodeRepository verificationCodeRepository;
    private final RabbitTemplate rabbitTemplate;
    private final AccountRepository accountRepository;

    
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
    @Transactional
    public TwoFactorLoginResponse login(LoginDto loginDto) {
        try {
            Users user = usersRepository.findByUsername(loginDto.getUsername())
                    .orElseThrow(()-> new NullPointerException(String.format("username not found : %s",loginDto.getUsername())));

            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

            if (Boolean.TRUE.equals(user.getTwoFactorEnabled())) {
                String verificationCode = generateVerificationCode();
                saveVerificationCode(user.getId(), verificationCode);
                sendTwoFactorCodeViaRabbitMQ(user.getEmail(), user.getUsername(), verificationCode);
                
                return TwoFactorLoginResponse.builder()
                        .requiresTwoFactor(true)
                        .message("Two-factor authentication code sent to your email")
                        .build();
            } else {
                // normal login
                UserImpl userImpl = convertToUserImpl(user);
                user.setLastLogin(LocalDateTime.now());
                usersRepository.save(user);
                String token = jwtService.generateToken(userImpl);
                
                return TwoFactorLoginResponse.builder()
                        .requiresTwoFactor(false)
                        .tokenResponse(TokenResponse.builder().token(token).build())
                        .build();
            }
        }
        catch (AuthenticationException e){
            log.info("Password is not correct.");
            throw e;
        }
    }


    @Transactional
    public Users saveUser(SignUpDto sign){
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
        return user;
    }



    @Override
    @Transactional
    public void signup(SignUpDto sign) {
        Users user= saveUser(sign);
        CreateAccountDTO accountDTO = CreateAccountDTO.builder()
                .userId(usersRepository.findByUsername(user.getUsername()).get().getId())
                .accountType("TRY")
                .build();
        createAccount(accountDTO);
    }

    @Transactional
    public void createAccount(CreateAccountDTO dto)
    {
        log.info("save started");
        accountRepository.save(Account.builder()
                .userId(usersRepository.getReferenceById(dto.getUserId()))
                .accountType(Account.AccountType.valueOf(dto.getAccountType()))
                .balance(Constants.INITIAL_BALANCE)
                .transferCode(getTransferCode())
                .build());
        log.info("save finished");
    }

    @Override
    public void changePassword(PasswordChangeRequest request) {
        Users user = usersRepository.findById(request.userId).orElseThrow(
                ()-> new GenericFinanceException("generic.auth.userNotFound"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new GenericFinanceException("user.profile.incorrectPassword");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }

    @Override
    public TokenResponse verifyTwoFactorCode(TwoFactorLoginRequest request) {
        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new GenericFinanceException("generic.auth.userNotFound"));

        VerificationCode verificationCode = verificationCodeRepository
                .findByUserIdAndCodeAndNotUsedAndType(
                        user.getId(), 
                        request.getVerificationCode(), 
                        VerificationCode.VerificationType.TWO_FACTOR_AUTH)
                .orElseThrow(() -> new GenericFinanceException("generic.auth.invalidVerificationCode"));

        if (verificationCode.isExpired()) {
            throw new GenericFinanceException("generic.auth.expiredVerificationCode");
        }

        verificationCode.setIsUsed(true);
        verificationCodeRepository.save(verificationCode);

        UserImpl userImpl = convertToUserImpl(user);
        user.setLastLogin(LocalDateTime.now());
        usersRepository.save(user);
        String token = jwtService.generateToken(userImpl);

        return TokenResponse.builder().token(token).build();
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public void saveVerificationCode(Long userId, String code) {
        VerificationCode verificationCode = VerificationCode.builder()
                .userId(userId)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .verificationType(VerificationCode.VerificationType.TWO_FACTOR_AUTH)
                .build();
        
        verificationCodeRepository.save(verificationCode);
    }

    private void sendTwoFactorCodeViaRabbitMQ(String email, String username, String code) {
        try {
            TwoFactorEmailMessage message = TwoFactorEmailMessage.builder()
                    .email(email)
                    .username(username)
                    .code(code)
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.TWO_FACTOR_EMAIL_EXCHANGE,
                    RabbitMQConfig.TWO_FACTOR_EMAIL_ROUTING_KEY,
                    message
            );
            
            log.info("Two-factor authentication code message sent to queue for: {}", email);
        } catch (Exception e) {
            log.error("Failed to send two-factor authentication code message to queue for: {}", email, e);
            throw new GenericFinanceException("generic.auth.code.fail");
        }
    }

    @Override
      
    public void enable2FA(Enable2FARequest request) {
        Users user = usersRepository.findById(request.getUserId())
                .orElseThrow(() -> new GenericFinanceException("generic.auth.userNotFound"));
        
        user.setTwoFactorEnabled(request.getEnable2FA());
        usersRepository.save(user);
        
        log.info("2FA {} for user: {}", request.getEnable2FA() ? "enabled" : "disabled", user.getUsername());
    }

    @Override
    public TokenVerificationResponse verifyToken(String token) {
        try {
            String username = jwtService.getUsernameFromToken(token);
            Users user = usersRepository.findByUsername(username)
                    .orElseThrow(() -> new GenericFinanceException("generic.auth.userNotFound"));
            UserImpl userImpl = convertToUserImpl(user);
            boolean isValid = jwtService.validateToken(token, userImpl);
            if (isValid) {
                return new TokenVerificationResponse(true, username, user.getId());
            } else {
                return new TokenVerificationResponse(false, null, null);
            }
        } catch (Exception e) {
            log.warn("Token verification failed: {}", e.getMessage());
            return new TokenVerificationResponse(false, null, null);
        }
    }


    private String getTransferCode(){
        String transferCode;
        do {
            transferCode = generateTransferCode();
        }
        while (accountRepository.existsByTransferCode(transferCode));
        return transferCode;
    }
    private String generateTransferCode() {
        Random rand = new Random();
        int LENGTH = Constants.TRANSFER_CODE_CHARSET.length();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < Constants.TRANSFER_CODE_CHARSET.length(); i++) {
            sb.append(Constants.TRANSFER_CODE_CHARSET.charAt(rand.nextInt(LENGTH)));
        }
        return sb.toString();
    }
}
