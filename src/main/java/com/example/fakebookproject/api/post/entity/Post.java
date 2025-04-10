package com.example.fakebookproject.api.post.entity;

import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.common.config.CustomBaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Post extends CustomBaseTimeEntity {

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

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

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

    public void updatePost(String contents, String imageUrl){
        if(contents != null) this.contents = contents;
        if(imageUrl != null) this.imageUrl = imageUrl;
        updatedAt = LocalDateTime.now();
    }
}
