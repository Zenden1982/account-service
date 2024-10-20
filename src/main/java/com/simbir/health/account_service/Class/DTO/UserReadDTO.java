package com.simbir.health.account_service.Class.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDTO {

    private Long id;

    private String lastName;

    private String firstName;

    private String username;

}
