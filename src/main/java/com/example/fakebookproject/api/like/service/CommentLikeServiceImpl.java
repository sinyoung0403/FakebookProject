package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.like.repository.CommentLikeRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    @Override
    public void createCommentLike(Long postId, Long commentId, LoginRequestDto loginRequestDto) {
        // Data 검증
        // 1. 데이터 검증 및 조회
        // Post 는 검증만 잘못된 URL 이라고만 알려주기.
        User findUser = userRepository.findUserByEmailOrElseThrow(loginRequestDto.getEmail());
//        Post findPost = postRepository.existsById(postId);
//        Comment comment =

    }

    @Override
    public List<FindAllCommentLikeResponseDto> findAllCommentLike(Long postId, Long commentId, LoginRequestDto loginRequestDto) {
        return List.of();
    }

    @Override
    public void deleteCommentLike(Long postId, Long commentId, LoginRequestDto loginRequestDto) {

    }
}
