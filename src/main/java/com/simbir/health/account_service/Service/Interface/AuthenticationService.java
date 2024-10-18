package com.simbir.health.account_service.Service.Interface;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.LoginDTO;

public interface AuthenticationService extends UserDetailsService {

    String signUp(AccountCreateDTO createAccountDTO);

    TokenPair signIn(LoginDTO loginDTO);

    void signOut(String refreshToken);

    Boolean validate(String token);

    TokenPair refreshTokens(String refreshToken);
}
