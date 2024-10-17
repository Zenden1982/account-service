package com.simbir.health.account_service.Controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.LoginDTO;
import com.simbir.health.account_service.Service.Interface.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Authentication")
public class AuthethenticationController {

    @Lazy
    private final AuthenticationService accountService;

    @PostMapping("SignUp")
    public ResponseEntity<String> signUp(@RequestBody AccountCreateDTO createAccountDTO) {
        String answer = accountService.signUp(createAccountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }

    @PostMapping("SignIn")
    public ResponseEntity<String> signIn(@RequestBody LoginDTO loginDTO) {
        String token = accountService.signIn(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PutMapping("SignOut")
    public ResponseEntity<Void> signOut() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("test")
    public String getMethodName() {
        return "test";
    }

}
