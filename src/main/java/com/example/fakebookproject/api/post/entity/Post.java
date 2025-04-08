package com.example.fakebookproject.api.post.entity;

import com.example.fakebookproject.common.config.BaseTimeEntity;
import com.example.fakebookproject.api.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @Column(name="image_url")
    private String imageUrl;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Column(nullable = false)
    private Long likeCount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Post(String contents, String imageUrl, User user){
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.user = user;
        isDeleted = false;
        likeCount = 0L;
    }

    public Post() {
        isDeleted = false;
    }
}
