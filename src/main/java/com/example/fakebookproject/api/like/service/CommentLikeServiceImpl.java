package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.comment.repository.CommentRepository;
import com.example.fakebookproject.api.like.dto.FindAllCommentLikeResponseDto;
import com.example.fakebookproject.api.like.entity.CommentLike;
import com.example.fakebookproject.api.like.repository.CommentLikeRepository;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
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

    /**
     * Comment 에 Like 추가
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException Post 가 존재하지 않으면 예외 발생 (NOT_FOUND_POST)
     * @throws CustomException Comment 가 존재하지 않으면 예외 발생 (NOT_FOUND_COMMENT)
     * @throws CustomException User 가 존재하면 예외 발생 (USER_ALREADY_EXISTS)
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (LIKE_FAILED)
     */
    @Transactional
    @Override
    public void createCommentLike(Long postId, Long commentId, Long loginUserId) {

        // 1. Like 추가 전 유효성 검사:
        User findUser = userRepository.findUserByIdOrElseThrow(loginUserId);
        postRepository.validateExistenceByPost_Id(postId);
        Comment findComment = commentRepository.findCommentByIdOrElseThrow(commentId);

        // 2. commentLikeRepository User 존재 여부 유효성 검사
        commentLikeRepository.validateNotExistenceByUserIdAndCommentId(loginUserId, commentId);

        // 3. 본인 댓글인지 확인
        if (findComment.getUser().getId().equals(loginUserId)) {
            throw new CustomException(ExceptionCode.CANNOT_LIKE_OWN);
        }

        // 4. Entity 생성
        CommentLike commentLike = new CommentLike(findUser, findComment);

        // 5. commentLikeRepository 추가
        commentLikeRepository.save(commentLike);

        // 6. commentRepository 에서 like count Column +1
        commentRepository.increaseLikeCount(commentId);
    }

    /**
     * Comment 에 추가된 Like 모두 조회
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @return 좋아요를 누른 사용자 (사용자 이름, 이미지 URL) List
     */
    @Override
    public List<FindAllCommentLikeResponseDto> findAllCommentLike(Long postId, Long commentId, Long loginUserId) {
        return commentLikeRepository.findAllByComment_Id(commentId)
                .stream()
                .map(FindAllCommentLikeResponseDto::new)
                .toList();
    }

    /**
     * Comment 에 추가된 Like 삭제
     *
     * @param postId
     * @param commentId
     * @param loginUserId
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException Post 가 존재하지 않으면 예외 발생 (NOT_FOUND_POST)
     * @throws CustomException Comment 가 존재하지 않으면 예외 발생 (NOT_FOUND_COMMENT)
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException User, Comment 가 존재하지 않으면 예외 발생 (NOT_FOUND_POST)
     */
    @Transactional
    @Override
    public void deleteCommentLike(Long postId, Long commentId, Long loginUserId) {

        // 1. Like 추가 전 유효성 검사:
        userRepository.validateExistenceByUserId(loginUserId);
        postRepository.validateExistenceByPost_Id(postId);
        commentRepository.validateExistenceByCommentId(commentId);

        // 2. commentLikeRepository User 존재 여부 유효성 검사
        commentLikeRepository.validateExistenceByUserIdAndCommentId(loginUserId, commentId);

        // 3. Entity 생성
        CommentLike commentLike = commentLikeRepository.findByCommentOrElseThrow(commentId, postId, loginUserId);

        // 4. commentLikeRepository 삭제
        commentLikeRepository.delete(commentLike);

        // 5. commentRepository 에서 like count Column -1
        commentRepository.decreaseLikeCount(postId);
    }
}
