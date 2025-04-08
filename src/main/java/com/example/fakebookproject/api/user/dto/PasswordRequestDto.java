package com.example.fakebookproject.api.user.dto;

import lombok.Getter;

@Getter
public class PasswordRequestDto {

    private final String password;

    public PasswordRequestDto(String password) {
        this.password = password;
    }

}
