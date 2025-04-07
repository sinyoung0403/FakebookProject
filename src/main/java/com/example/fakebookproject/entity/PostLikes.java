package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "post_like")
@NoArgsConstructor
public class PostLikes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Posts posts;

  public void setUsers(Users users) {
    this.users = users;
  }

  public void setPosts(Posts posts) {
    this.posts = posts;
  }
}

