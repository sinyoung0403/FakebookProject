package com.example.fakebookproject.api.comment.entity;

import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.common.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likeCount;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.likeCount = 0L;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void addLikeCount() {
        this.likeCount++;
    }

    public void subLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}