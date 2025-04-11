package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;
import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.like.repository.PostLikeRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.dto.LoginRequestDto;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Post 에 Like 추가
     *
     * @param postId
     * @param loginUserId
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException Post 가 존재하지 않으면 예외 발생 (NOT_FOUND_POST)
     * @throws CustomException User 가 존재하면 예외 발생 (USER_ALREADY_EXISTS)
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (LIKE_FAILED)
     */
    @Transactional
    @Override
    public void createPostLike(Long postId, Long loginUserId) {
        // 1. Like 추가 전 유효성 검사:
        // - User 가 존재하지 않으면 예외 발생
        // - Post 이 존재하지 않으면 예외 발생
        User findUser = userRepository.findUserByIdOrElseThrow(loginUserId);
        Post findPost = postRepository.findPostByIdOrElseThrow(postId);

        // 2. postLikeRepository User 존재 여부 유효성 검사
        postLikeRepository.validateNotExistenceByUserIdAndPostId(loginUserId, postId);

        // 3. 본인 게시물인지 확인
        if (findPost.getUser().getId().equals(loginUserId)) {
            throw new CustomException(ExceptionCode.CANNOT_LIKE_OWN);
        }

        // 4. Entity 생성
        PostLike postLike = new PostLike(findUser, findPost);

        // 5. postLikeRepository 추가
        postLikeRepository.save(postLike);

        // 6. PostRepository 에서 like count Column +1
        postRepository.increaseLikeCount(postId);
    }

    /**
     * Post 에 추가된 Like 모두 조회
     *
     * @param postId
     * @return 좋아요를 누른 사용자 (사용자 이름, 이미지 URL) List
     */
    @Override
    public List<FindAllPostLikeResponseDto> findAllPostLike(Long postId) {
        // 해당 Post Id 의 Like 를 추가한 사용자 모두 조회
        return postLikeRepository.findAllByPost_Id(postId)
                .stream()
                .map(FindAllPostLikeResponseDto::new).toList();
    }

    /**
     * Post 에 추가된 Like 삭제
     *
     * @param postId
     * @param loginUserId
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     * @throws CustomException Post 가 존재하지 않으면 예외 발생 (NOT_FOUND_POST)
     * @throws CustomException User 가 존재하지 않으면 예외 발생 (NOT_FOUND_USER)
     */
    @Transactional
    @Override
    public void deletePostLike(Long postId, Long loginUserId) {
        // 1. Like 추가 전 유효성 검사:
        userRepository.validateExistenceByUserId(loginUserId);
        postRepository.validateExistenceByPost_Id(postId);

        // 2. postLikeRepository User 존재 여부 유효성 검사
        postLikeRepository.validateExistenceByUserId(loginUserId, postId);

        // 3. Entity 생성
        PostLike postLike = postLikeRepository.findByPost_IdAndUser_IdOrElseThrow(postId, loginUserId);

        // 4. postLikeRepository 삭제
        postLikeRepository.delete(postLike);

        // 5. PostRepository 에서 like count Column -1
        postRepository.decreaseLikeCount(postId);
    }
}
