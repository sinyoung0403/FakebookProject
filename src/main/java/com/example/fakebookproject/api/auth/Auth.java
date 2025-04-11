package com.example.fakebookproject.api.auth;

import com.example.fakebookproject.api.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Table(name = "auth")
@Entity
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Auth(String refreshToken, User user) {
        this.refreshToken = refreshToken;
        this.user = user;
    }

    // refreshToken 재발급
    public void refreshUpdate(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


