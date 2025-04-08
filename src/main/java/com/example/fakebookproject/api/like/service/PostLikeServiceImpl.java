package com.example.fakebookproject.api.like.service;

import com.example.fakebookproject.api.like.entity.PostLike;
import com.example.fakebookproject.api.like.repository.PostLikeRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;

    /**
     * Post Like 추가
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

}
