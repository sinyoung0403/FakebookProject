package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;
import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.like.repository.PostLikeRepository;
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
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Post 에 Like 추가
     *
     * @param postId
     */
    @Transactional
    @Override
    public void createPostLike(Long postId, Long loginUserId) {
        // 1. 데이터 검증 및 조회 . 실제 테이블이 존재하는 가.
        // 세션 생길 시
        User findUser = userRepository.findUserByIdOrElseThrow(loginUserId);
        Post findPost = postRepository.findPostByIdOrElseThrow(postId);

        // 2. 해당 user 가 이미 포스트 라이크에 있으면 취소 .
        postLikeRepository.validateNotExistenceByUserId(loginUserId);

        // 2. Entity 로 변환.
        PostLike postLike = new PostLike(findUser, findPost);

        // 3. 좋아요 추가 , Post 에 있는 Like count 로 증가
        postLikeRepository.save(postLike);

        // 4. post count 에도 추가.
        postRepository.increaseLikeCount(postId);
    }

    /**
     * Post 에 추가된 Like 모두 조회
     *
     * @param postId
     * @return
     */
    @Override
    public List<FindAllPostLikeResponseDto> findAllPostLike(Long postId) {
        return postLikeRepository.findAllByPost_Id(postId)
                .stream()
                .map(FindAllPostLikeResponseDto::from).toList();
    }

    /**
     * Post 에 추가된 Like 삭제
     *
     * @param postId
     */
    @Override
    public void deletePostLike(Long postId, Long loginUserId) {
        // 1. 데이터 검증 및 조회 . 실제 테이블이 존재하는 가. 존재할때 에러 떠야함.
        // User 부분에
        userRepository.validateExistenceByUserId(loginUserId);
//        postRepository.validateExistenceByPost_Id(postId);

        // 2. 존재하는지 확인
        postLikeRepository.validateExistenceByUserId(loginUserId);

        // 3. Entity 로 변환 추후에 변경해야함.
        PostLike postLike = postLikeRepository.findByPost_IdAndUser_IdOrElseThrow(postId, loginUserId);

        // 4. 삭제
        postLikeRepository.delete(postLike);

        // 5. 포스트 테이블의 like count -1 해주어야함.
         postRepository.decreaseLikeCount(postId);
    }
}
