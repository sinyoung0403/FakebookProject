package com.example.fakebookproject.api.user.service;

import com.example.fakebookproject.api.user.dto.UserCreateRequestDto;
import com.example.fakebookproject.api.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto dto);

}
