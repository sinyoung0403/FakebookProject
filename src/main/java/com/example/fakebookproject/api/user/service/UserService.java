package com.example.fakebookproject.api.user.service;

import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.dto.PasswordRequestDto;
import com.example.fakebookproject.api.user.dto.UserCreateRequestDto;
import com.example.fakebookproject.api.user.dto.UserResponseDto;
import com.example.fakebookproject.api.user.entity.User;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto dto);

    UserResponseDto findUserById(Long userId);

    UserResponseDto findUserByLoginUserId(Long loginUserId, PasswordRequestDto dto);

    User loginUser(LoginRequestDto dto);

}
