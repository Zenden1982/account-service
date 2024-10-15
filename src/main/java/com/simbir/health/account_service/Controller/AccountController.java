package com.simbir.health.account_service.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Service.Interface.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountCreateDTO createAccountDTO) {
        String answer = accountService.registation(createAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }
}
