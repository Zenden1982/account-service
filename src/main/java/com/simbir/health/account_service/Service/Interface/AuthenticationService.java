package com.simbir.health.account_service.Service.Interface;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.DTO.UserLoginDTO;
import com.simbir.health.account_service.Class.DTO.UserRegistrationDTO;

public interface AuthenticationService extends UserDetailsService {

    String signUp(UserRegistrationDTO userRegistrationDTO);

    TokenPair signIn(UserLoginDTO userLoginDTO);

    void signOut(String refreshToken);

    Boolean validate(String token);

    TokenPair refreshTokens(String refreshToken);
}
