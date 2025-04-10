package com.example.fakebookproject.api.user.repository;

import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.aspectj.weaver.ast.And;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(Long userId);

    boolean existsByEmail(String email);

    boolean existsByIdAndIsDeletedFalse(Long userId);

    boolean existsAllByEmail(String email);

    Optional<User> findUserById(Long userId);

    Optional<User> findUserByEmail(String email);

    default void validateExistenceByUserId(Long userId) {
        if (!existsByIdAndIsDeletedFalse(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default void validateNotExistenceByUserId(Long userId) {
        if (existsById(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default void validateNotExistenceByUserEmail(String email) {
        if (existsAllByEmail(email)) {
            throw new CustomException(ExceptionCode.DUPLICATE_EMAIL);
        }
    }

    default User findUserByIdOrElseThrow(Long userId) {
        return findUserById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    default User findUserByEmailOrElseThrow(String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.LOGIN_FAILED));
    }

    Page<User> findByIdIn(Collection<Long> ids, Pageable pageable);
}
