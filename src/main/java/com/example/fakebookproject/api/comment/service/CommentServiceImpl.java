package com.example.fakebookproject.api.comment.service;

import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.comment.repository.CommentRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     *
     * @param userId 현재 로그인된 사용자 식별자
     * @param postId 댓글을 등록할 게시글 식별자
     * @param content 등록할 댓글 내용
     * @return 생성된 댓글 정보
     */
    @Override
    public CommentResponseDto createComment(Long userId, Long postId, String content) {
        User user = getUserById(userId);
        Post post = getPostById(postId);

        Comment comment = new Comment(user, post, content);
        Comment saved = commentRepository.save(comment);

        return new CommentResponseDto(saved);
    }

    /**
     * 댓글 목록 조회
     *
     * @param postId 조회할 게시글 식별자
     * @param pageable 페이징 정보
     * @return 조회된 댓글 목록 (페이징 처리 포함)
     */
    @Override
    public Page<CommentResponseDto> findAllComments(Long postId, Pageable pageable) {
        Post post = getPostById(postId);
        Page<Comment> comments = commentRepository.findByPost(post, pageable);

        return comments.map(CommentResponseDto::new);
    }

    /**
     * 댓글 수정
     *
     * @param userId 현재 로그인된 사용자 식별자
     * @param commentId 수정할 댓글 식별자
     * @param content 수정할 댓글 내용
     * @return 성공 시 수정된 댓글 내용
     *         - 수정할 댓글이 존재하지 않을 경우 NOT_FOUND_COMMENT 응답
     *         - 댓글 작성자와 현재 로그인된 사용자가 일치하지 않을 경우 UNAUTHORIZED_ACCESS 응답
     *         - 변경할 내용이 없을 경우 NO_CHANGES 응답
     */
    @Override
    @Transactional
    public CommentResponseDto updateComment(Long userId, Long commentId, String content) {
        Comment comment = getCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        if (content == null || comment.getContent().equals(content)) {
            throw new CustomException(ExceptionCode.NO_CHANGES);
        }

        comment.updateContent(content);

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글 삭제
     * - 성공 시 204 No Content 응답
     * - 삭제할 댓글이 존재하지 않을 경우 NOT_FOUND_COMMENT 응답
     * - 댓글 작성자와 현재 로그인된 사용자가 일치하지 않을 경우 UNAUTHORIZED_ACCESS 응답
     *
     * @param userId 현재 로그인된 사용자 식별자
     * @param commentId 삭제할 댓글 식별자
     */
    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        commentRepository.delete(comment);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_COMMENT));
    }
}
