package com.simbir.health.account_service.Class.DTO;

import lombok.Data;

@Data
public class AccountCreateDTO {

    private String firstName;

    private String lastName;

    private String username;

    private String password;
}
