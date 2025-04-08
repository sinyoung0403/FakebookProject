package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "friend_status")
@NoArgsConstructor
public class FriendStatus {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="users_id")
    private Users requestUser;

    @ManyToOne
    @JoinColumn(name="users_id")
    private Users responseUser;

    @Column
    private int status;
}
