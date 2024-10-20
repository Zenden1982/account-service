package com.simbir.health.account_service.Controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simbir.health.account_service.Class.DTO.AdminCreateUpdateUserDTO;
import com.simbir.health.account_service.Class.DTO.UserReadDTO;
import com.simbir.health.account_service.Class.DTO.UserUpdateDTO;
import com.simbir.health.account_service.Service.Interface.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("Accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("Me")
    public ResponseEntity<UserReadDTO> getUserInformation(HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getUserInformation(request.getHeader("Authorization").substring(7)));
    }

    @PutMapping("Update")
    public ResponseEntity<Void> updateUserInformation(HttpServletRequest request,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        accountService.updateUser(userUpdateDTO, request.getHeader("Authorization").substring(7));
        return ResponseEntity.status(200).build();
    }

    @GetMapping("All")
    public ResponseEntity<Page<UserReadDTO>> getAllUser(Integer page, Integer size) {
        return ResponseEntity.ok(accountService.getAllUsers(page, size));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody AdminCreateUpdateUserDTO user) {
        accountService.createUserByAdmin(user);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateUser(@RequestBody AdminCreateUpdateUserDTO user, @PathVariable Long id) {
        accountService.updateUserByAdmin(user);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        accountService.deleteUserByAdmin(id);
        return ResponseEntity.status(200).build();
    }
}
