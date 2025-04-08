package com.example.fakebookproject.api.user.repository;

import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(Long userId);

    boolean existsByEmail(String email);

    Optional<User> findUserById(Long userId);

    Optional<User> findUserByEmail(String email);


    default void validateExistenceByUserId(Long userId) {
        if (!existsById(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default void validateExistenceByUserEmail(String email) {
        if (!existsByEmail(email)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default User findUserByEmailOrElseThrow(String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.LOGIN_FAILED));
    }

    default User findUserByIdOrElseThrow(Long userId) {
        return findUserById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

}
