package com.example.fakebookproject.api.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUserId(Long userId);

    default Auth findByUserIdOrNull(Long userId) {
        return findByUserId(userId).orElse(null);
    }

    void deleteAuthByRefreshToken(String refreshToken);

    void deleteByRefreshTokenExpireTimeBefore(Date date);
}