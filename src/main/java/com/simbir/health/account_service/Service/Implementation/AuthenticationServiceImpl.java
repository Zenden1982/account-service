package com.simbir.health.account_service.Service.Implementation;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.User;
import com.simbir.health.account_service.Class.DTO.AccountCreatedDTO;
import com.simbir.health.account_service.Class.DTO.UserLoginDTO;
import com.simbir.health.account_service.Class.DTO.UserRegistrationDTO;
import com.simbir.health.account_service.Repository.UserRepository;
import com.simbir.health.account_service.Service.Interface.AuthenticationService;
import com.simbir.health.account_service.Util.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final KafkaTemplate<String, AccountCreatedDTO> kafkaTemplate;

    private final UserRepository userRepository;

    private final JwtTokenUtils jwtTokenUtils;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String signUp(UserRegistrationDTO userRegistrationDTO) {
        String accountId = UUID.randomUUID().toString();
        AccountCreatedDTO accountCreatedDTO = new AccountCreatedDTO(accountId, userRegistrationDTO.getFirstName(),
                userRegistrationDTO.getLastName(), userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword());

        User user = User.builder()
                .firstName(userRegistrationDTO.getFirstName())
                .lastName(userRegistrationDTO.getLastName())
                .username(userRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .build();

        userRepository.save(user);
        CompletableFuture<SendResult<String, AccountCreatedDTO>> future = kafkaTemplate.send("account.events.created",
                accountId, accountCreatedDTO);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message='{}' due to: {}", accountCreatedDTO, exception.getMessage());
            } else {
                log.info("sent message='{}' with offset={}", accountCreatedDTO, result.getRecordMetadata().offset());
            }
        });
        return "OK";
    }

    @Override
    public TokenPair signIn(UserLoginDTO userLoginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));

        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }
        UserDetails userDetails = loadUserByUsername(userLoginDTO.getUsername());
        return new TokenPair(jwtTokenUtils.generateRefreshToken(userDetails),
                jwtTokenUtils.generateAccessToken(userDetails));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username));
        if (user.isEnabled()) {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    user.getAuthorities());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

    }

    @Override
    public void signOut(String token) {
        jwtTokenUtils.signOut(token);
    }

    @Override
    public Boolean validate(String token) {
        try {
            return !jwtTokenUtils.getClaimsFromToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public TokenPair refreshTokens(String refreshToken) {
        return jwtTokenUtils.refresh(refreshToken,
                loadUserByUsername(jwtTokenUtils.getUsernameFromToken(refreshToken)));

    }
}
