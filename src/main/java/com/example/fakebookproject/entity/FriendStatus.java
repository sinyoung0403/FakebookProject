package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class FriendStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int status;

    @ManyToOne
    @JoinColumn(name = "request_user_id", nullable = false)
    private Users requestUser;

    @ManyToOne
    @JoinColumn(name = "response_user_id", nullable = false)
    private Users responseUser;
}
