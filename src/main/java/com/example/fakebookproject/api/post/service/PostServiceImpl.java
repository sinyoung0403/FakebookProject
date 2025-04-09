package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.friend.repository.FriendRepository;
import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.dto.PostUpdateDto;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

    @Override
    public PostResponseDto findPostById(Long id) {
        Post post = postRepository.findPostByIdOrElseThrow(id);
        return null;
    }

    @Override
    public void createPost(PostCreateRequestDto requestDto, Long loginId) {

        User foundUser = userRepository.findUserByIdOrElseThrow(loginId);

        Post post = new Post(
                requestDto.getContents(),
                requestDto.getImageUrl(),
                foundUser
        );

        Post savedPost = postRepository.save(post);

    }

    @Override
    public Page<PostResponseDto> findMyPost(Long loginId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postPage = postRepository.findPostByUserIdOrElseThrow(loginId, pageable);

        return postPage.map(PostResponseDto::new);
    }

    @Override
    public Page<PostResponseDto> findRelatedPost(Long loginId, int page, int size) {

        List<Long> friendIds = friendRepository.findAllByUserIdAndStatusAccepted(loginId);

        friendIds.add(loginId);

        Page<Post> posts = postRepository.findPostByUserIdInOrElseThrow(friendIds, PageRequest.of(page,size));

        return posts.map(PostResponseDto::new);

    }

    @Transactional
    @Override
    public void updatePost(Long id, Long loginId, PostUpdateDto requestDto) {

        Post foundPost = postRepository.findPostByIdOrElseThrow(id);

        if(!foundPost.getUser().getId().equals(loginId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        foundPost.updatePost(requestDto.getContents(), requestDto.getImageUrl());
    }

    @Override
    public void deletePost(Long id, Long loginId) {

        Post foundPost = postRepository.findPostByIdOrElseThrow(id);

        if(!foundPost.getUser().getId().equals(loginId)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_ACCESS);
        }

        postRepository.delete(foundPost);

    }


}
