package com.simbir.health.account_service.Service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.simbir.health.account_service.Class.DTO.AdminCreateUpdateUserDTO;
import com.simbir.health.account_service.Class.DTO.UserReadDTO;
import com.simbir.health.account_service.Class.DTO.UserUpdateDTO;

@Service
public interface AccountService {

    UserReadDTO getUserInformation(String token);

    void updateUser(UserUpdateDTO userUpdateDTO, String token);

    Page<UserReadDTO> getAllUsers(Integer page, Integer size);

    void createUserByAdmin(AdminCreateUpdateUserDTO user);

    void updateUserByAdmin(AdminCreateUpdateUserDTO user);

    void deleteUserByAdmin(Long id);
}
