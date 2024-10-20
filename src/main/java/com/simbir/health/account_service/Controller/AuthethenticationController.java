package com.simbir.health.account_service.Controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simbir.health.account_service.Class.TokenPair;
import com.simbir.health.account_service.Class.DTO.UserLoginDTO;
import com.simbir.health.account_service.Class.DTO.UserRegistrationDTO;
import com.simbir.health.account_service.Service.Interface.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Authentication")
@Slf4j
public class AuthethenticationController {

    @Lazy
    private final AuthenticationService authenticationService;

    @PostMapping("SignUp")
    public ResponseEntity<String> signUp(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        String answer = authenticationService.signUp(userRegistrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(answer);
    }

    @PostMapping("SignIn")
    public ResponseEntity<TokenPair> signIn(@RequestBody UserLoginDTO loginDTO) {
        TokenPair token = authenticationService.signIn(loginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PutMapping("SignOut")
    public ResponseEntity<Void> signOut(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        authenticationService.signOut(token.substring(7));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("Refresh")
    public ResponseEntity<TokenPair> refreshTokens(@RequestBody String refreshToken) {
        TokenPair tokens = authenticationService.refreshTokens(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @GetMapping("Validate")
    public ResponseEntity<Boolean> validate(@RequestParam String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.validate(token));
    }

    @GetMapping("test")
    public String getMethodName(HttpServletRequest request) {
        log.info(request.toString());
        return "test";
    }

}
