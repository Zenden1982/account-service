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

import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.DTO.AccountCreateDTO;
import com.simbir.health.account_service.Class.DTO.LoginDTO;
import com.simbir.health.account_service.Service.Interface.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<TokenPair> signIn(@RequestBody LoginDTO loginDTO) {
        TokenPair token = accountService.signIn(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PutMapping("SignOut")
    public ResponseEntity<Void> signOut(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        accountService.signOut(token.substring(7));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("Refresh")
    public ResponseEntity<TokenPair> refreshTokens(@RequestBody String refreshToken) {
        TokenPair tokens = accountService.refreshTokens(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @GetMapping("test")
    public String getMethodName() {
        return "test";
    }

}
