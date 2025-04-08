package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.comment.repository.CommentRepository;
import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.like.entity.CommentLike;
import com.example.fakebookproject.api.like.repository.CommentLikeRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void createCommentLike(Long postId, Long commentId, Long loginUserId) {
        // 1. 데이터 검증 및 조회
        User findUser = userRepository.findUserByIdOrElseThrow(loginUserId);
        postRepository.findPostByIdOrElseThrow(postId);
        Comment findComment = commentRepository.findCommentByIdOrElseThrow(commentId);

        // 2. Entity 변환
        CommentLike commentLike = new CommentLike(findUser, findComment);

        // 3. Comment 좋아요 추가
        commentLikeRepository.save(commentLike);

        // 4. Comment Table Like Count 추가
        commentRepository.increaseLikeCount(commentId);
    }

    @Override
    public List<FindAllCommentLikeResponseDto> findAllCommentLike(Long postId, Long commentId, Long loginUserId) {
        return commentLikeRepository.findAllByComment_Id(commentId)
                .stream()
                .map(FindAllCommentLikeResponseDto::from).toList();
    }

    @Override
    public void deleteCommentLike(Long postId, Long commentId, Long loginUserId) {
        // 1. 데이터 검증 및 조회
        userRepository.validateNotExistenceByUserId(loginUserId);
        postRepository.validateNotExistenceByPost_Id(postId);
        commentRepository.validateNotExistenceByCommentId(commentId);

        // 2. Entity 변환
        CommentLike commentLike = commentLikeRepository.findByComment_IdAndUser_IdOrElseThrow(postId, loginUserId);

        // 3. 삭제
        commentLikeRepository.delete(commentLike);
    }
}
