package com.example.fakebookproject.api.user.service;

import com.example.fakebookproject.api.user.dto.*;
import com.example.fakebookproject.api.user.entity.User;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto dto);

    UserResponseDto findUserById(Long userId);

    UserResponseDto findUserByLoginUserId(Long loginUserId);

    UserResponseDto updateUser(Long loginUserId, UserUpdateRequestDto dto);

    void updatePassword(Long loginUserId, UpdatePasswordRequestDto dto);

    void deleteUser(Long loginUserId, PasswordRequestDto dto);

    User loginUser(LoginRequestDto dto);

}
