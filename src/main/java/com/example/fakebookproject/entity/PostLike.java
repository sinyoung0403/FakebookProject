package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "post_like")
@NoArgsConstructor
public class PostLike {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post posts;

  public void setUsers(Users users) {
    this.users = users;
  }

  public void setPosts(Post posts) {
    this.posts = posts;
  }
}

