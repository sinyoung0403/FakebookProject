package com.example.fakebookproject.api.friend.entity;

import com.example.fakebookproject.api.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "friend_status")
public class FriendStatus {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_user_id")
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "response_user_id")
    private User responseUser;

    // 수락 1, 대기(요청) 0
    @Column(name = "status",
            columnDefinition = "INT(1) CHECK (status IN (1, 0))")
    private int status;

    public FriendStatus() {}

    public FriendStatus(User requestUser, User responseUser){
        this.requestUser = requestUser;
        this.responseUser = responseUser;
    }

    public FriendStatus(int status, User requestUser, User responseUser){
        this.status = status;
        this.requestUser = requestUser;
        this.responseUser = responseUser;
    }

    public void update(int status) {
        this.status = status;
    }
}
