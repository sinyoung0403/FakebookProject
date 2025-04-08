package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.Comment;

@Getter
@Entity
@Table(name = "comment_like")
@NoArgsConstructor
public class CommentLikes {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private Users users;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private Comment comment;

  public void setUsers(Users users) {
    this.users = users;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }
}
