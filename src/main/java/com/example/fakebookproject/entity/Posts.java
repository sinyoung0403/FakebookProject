package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Getter
@Entity
@NoArgsConstructor
@Check(constraints = "deleted IN ('Y', 'N')")
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Column(nullable = false)
    private Long likeCount;

    @Column(length = 1, nullable = false)
    private String deleted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
