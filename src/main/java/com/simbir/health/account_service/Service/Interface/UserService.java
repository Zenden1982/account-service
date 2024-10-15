package com.simbir.health.account_service.Service.Interface;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.LoginDTO;

public interface UserService extends UserDetailsService {

    String registation(AccountCreateDTO createAccountDTO);

    String login(LoginDTO loginDTO);
}
