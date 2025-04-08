package com.example.fakebookproject.api.post.service;

import com.example.fakebookproject.api.post.dto.PostCreateRequestDto;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void createPost(PostCreateRequestDto requestDto, HttpServletRequest request) {

        HttpSession session = request.getSession();

        Object sessionId = session.getAttribute("user");

        Long userId = null;

        if(sessionId instanceof Long){
            userId = (Long) sessionId;
        }

        User foundUser = findUserByIdOrElseThrow(userId);

        Post post = new Post(
                requestDto.getContents(),
                requestDto.getImageUrl(),
                foundUser
        );

        Post  savedPost = postRepository.save(post);

    }


    private User findUserByIdOrElseThrow(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE)
        );
    }
}
