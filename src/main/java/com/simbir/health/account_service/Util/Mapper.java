package com.simbir.health.account_service.Util;

import org.springframework.stereotype.Component;

import com.simbir.health.account_service.Class.User;
import com.simbir.health.account_service.Class.DTO.UserReadDTO;

@Component
public class Mapper {

    public UserReadDTO userToUserReadDTO(User user) {
        return new UserReadDTO(user.getId(), user.getLastName(), user.getFirstName(), user.getUsername());
    }
}
