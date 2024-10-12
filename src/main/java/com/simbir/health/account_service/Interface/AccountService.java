package com.simbir.health.account_service.Interface;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;

public interface AccountService {

    String createAccount(AccountCreateDTO createAccountDTO);
}
