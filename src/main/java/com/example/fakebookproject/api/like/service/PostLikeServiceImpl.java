package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.dto.FindAllPostLikeResponseDto;
import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.like.repository.PostLikeRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;

    /**
     * Post 에 좋아요 추가
     *
     * @param postId
     */
    @Override
    public void createPostLike(Long postId) {
        // 1. 데이터 검증 및 조회 . 실제 테이블이 존재하는 가.
        // 세션 생길 시
        User findUser = new User();
//        User user = UserRepository.findByIdOrElseThrow(userId);

        Post findPost = new Post();
//        Post findPost = PostRepository.findByIdOrElseThrow(postId);
        // 2. Entity 로 변환.
        PostLike postLike = new PostLike(findUser, findPost);

        // 3. 좋아요 추가
        postLikeRepository.save(postLike);
    }

    /**
     * Post 에 추가된 Like 모두 조회
     * @param postId
     * @return
     */
    @Override
    public List<FindAllPostLikeResponseDto> findAllLike(Long postId) {
        return postLikeRepository.findAllByPost_Id(postId)
                .stream()
                .map(FindAllPostLikeResponseDto::from).toList();
    }



}
