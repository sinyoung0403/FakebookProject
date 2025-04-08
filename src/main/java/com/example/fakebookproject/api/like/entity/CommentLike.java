package com.example.fakebookproject.api.like.entity;

import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.mapping.ToOne;

@Getter
@Entity
@Table(name = "comment_likes")
@NoArgsConstructor
public class CommentLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Comment comment;

  public void setUsers(User user) {
    this.user = user;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

}
