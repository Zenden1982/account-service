package com.simbir.health.account_service.Class.DTO;

import java.util.List;

import com.simbir.health.account_service.Class.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCreateUpdateUserDTO {

    private String lastName;

    private String firstName;

    private String username;

    private String password;

    private List<Role> roles;
}
