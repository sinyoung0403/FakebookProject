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

    Optional<User> findUserByIdAndIsDeletedFalse(Long userId);

    Optional<User> findUserByEmailAndIsDeletedFalse(String email);

    default void validateExistenceByUserId(Long userId) {
        if (!existsByIdAndIsDeletedFalse(userId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
    }

    default void validateNotExistenceByUserEmail(String email) {
        if (existsByEmail(email)) {
            throw new CustomException(ExceptionCode.DUPLICATE_EMAIL);
        }
    }

    default User findUserByIdOrElseThrow(Long userId) {
        return findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    default User findUserByEmailOrElseThrow(String email) {
        return findUserByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.LOGIN_FAILED));
    }

    Page<User> findByIdIn(Collection<Long> ids, Pageable pageable);
}
