package com.example.fakebookproject.api.friend.repository;

import com.example.fakebookproject.api.friend.entity.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendStatus, Long> {
}
