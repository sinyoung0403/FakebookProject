package com.example.fakebookproject.api.comment.service;

import com.example.fakebookproject.api.comment.dto.CommentResponseDto;
import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.comment.repository.CommentRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.dto.PageResponse;
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
        User user = userRepository.findUserByIdOrElseThrow(userId);
        Post post = postRepository.findPostByIdOrElseThrow(postId);

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
    public PageResponse<CommentResponseDto> findAllComments(Long postId, Pageable pageable) {
        Post post = postRepository.findPostByIdOrElseThrow(postId);
        Page<Comment> comments = commentRepository.findByPost(post, pageable);
        Page<CommentResponseDto> pageDto = comments.map(CommentResponseDto::new);

        return new PageResponse<>(
                pageDto.getContent(),
                pageDto.getNumber(),
                pageDto.getSize(),
                pageDto.getTotalElements(),
                pageDto.getTotalPages()
        );
    }

    /**
     * 댓글 수정
     *
     * @param loginUserId 현재 로그인된 사용자 식별자
     * @param commentId 수정할 댓글 식별자
     * @param content 수정할 댓글 내용
     * @return 성공 시 수정된 댓글 내용
     *         - 수정할 댓글이 있는 게시글이 존재하지 않을 경우 NOT_FOUND_POST 응답
     *         - 수정할 댓글이 존재하지 않을 경우 NOT_FOUND_COMMENT 응답
     *         - 댓글 작성자 또는 게시글 작성자가 현재 로그인된 사용자와 일치하지 않을 경우 UNAUTHORIZED_ACCESS 응답
     *         - 변경할 내용이 없을 경우 NO_CHANGES 응답
     */
    @Override
    @Transactional
    public CommentResponseDto updateComment(Long loginUserId, Long postId, Long commentId, String content) {
        postRepository.validateExistenceByPost_Id(postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));

        Long postUserId = post.getUser().getId();

        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);

        if (!comment.getUser().getId().equals(loginUserId) || !postUserId.equals(loginUserId)) {
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
     * - 삭제할 댓글이 있는 게시글이 존재하지 않을 경우 NOT_FOUND_POST 응답
     * - 삭제할 댓글이 존재하지 않을 경우 NOT_FOUND_COMMENT 응답
     * - 댓글 작성자 또는 게시글 작성자가 현재 로그인된 사용자와 일치하지 않을 경우 UNAUTHORIZED_ACCESS 응답
     *
     * @param loginUserId 현재 로그인된 사용자 식별자
     * @param commentId 삭제할 댓글 식별자
     */
    @Override
    @Transactional
    public void deleteComment(Long loginUserId, Long postId, Long commentId) {
        postRepository.validateExistenceByPost_Id(postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_POST));

        Long postUserId = post.getUser().getId();

        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);

        if (!comment.getUser().getId().equals(loginUserId) || !postUserId.equals(loginUserId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        commentRepository.delete(comment);
    }
}
