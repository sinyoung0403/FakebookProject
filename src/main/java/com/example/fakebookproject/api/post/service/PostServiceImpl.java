package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.friend.repository.FriendRepository;
import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.dto.PostResponseDto;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public void createPost(PostCreateRequestDto requestDto, HttpServletRequest request) {

        HttpSession session = request.getSession();

        Object sessionId = session.getAttribute("loginUser");

        Long userId = null;

        if(sessionId instanceof Long){
            userId = (Long) sessionId;
        }

        User foundUser = userRepository.findUserByIdOrElseThrow(userId);

        Post post = new Post(
                requestDto.getContents(),
                requestDto.getImageUrl(),
                foundUser
        );

        Post savedPost = postRepository.save(post);

    }

    @Override
    public Page<PostResponseDto> findMyPost(HttpServletRequest request, int page, int size) {
        HttpSession session = request.getSession();

        Object sessionId = session.getAttribute("loginUser");

        Long userId = null;

        if(sessionId instanceof Long){
            userId = (Long) sessionId;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postPage = postRepository.findPostByUserIdOrElseThrow(userId, pageable);

        return postPage.map(PostResponseDto::new);
    }

    @Override
    public Page<PostResponseDto> findRelatedPost(HttpServletRequest request, int page, int size) {
        HttpSession session = request.getSession();

        Object sessionId = session.getAttribute("user");

        Long userId = null;

        if(sessionId instanceof Long){
            userId = (Long) sessionId;
        }

        List<Long> friendIds = friendRepository.findAllByUserIdAndStatusAcceptedOrElseThrow(userId);

        Page<Post> posts = postRepository.findPostByUserIdInOrElseThrow(friendIds, PageRequest.of(page,size));

        return posts.map(PostResponseDto::new);

    }


}
