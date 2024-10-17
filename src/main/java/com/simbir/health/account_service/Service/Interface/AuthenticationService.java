package com.simbir.health.account_service.Service.Interface;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.LoginDTO;

public interface AuthenticationService extends UserDetailsService {

    String signUp(AccountCreateDTO createAccountDTO);

    String signIn(LoginDTO loginDTO);

    void signOut(String accessToken);

    Boolean validate(String token);

    String refreshAccessToken(String refreshToken);
}
