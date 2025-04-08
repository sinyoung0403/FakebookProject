package com.example.fakebookproject.api.user.repository;

import com.example.fakebookproject.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
