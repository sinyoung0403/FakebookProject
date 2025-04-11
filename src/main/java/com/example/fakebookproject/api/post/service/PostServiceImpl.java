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
import com.example.fakebookproject.common.dto.PageResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FriendRepository friendRepository;

    /**
     * 게시글 식별자로 게시글 찾기
     *
     * @param id    게시글 식별자
     * @return      해당 게시글
     */
    @Override
    public PostResponseDto findPostById(Long id) {
        Post findPost = postRepository.findPostByIdOrElseThrow(id);
        return new PostResponseDto(findPost);
    }

    /**
     * 게시글 생성
     *
     * @param requestDto        게시글 정보
     * @param loginId           사용자 식별자
     * @return                  저장된 정보
     * @throws CustomException  사용자 식별자가 존재하지 않을 경우 예외 발생
     */
    @Override
    public PostResponseDto createPost(PostCreateRequestDto requestDto, Long loginId) {

        User foundUser = userRepository.findUserByIdOrElseThrow(loginId);

        Post post = new Post(
                requestDto.getContents(),
                requestDto.getImageUrl(),
                foundUser
        );

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    /**
     * 내 게시글 찾기
     *
     * @param loginId       사용자 식별자
     * @param pageable      페이징 정보
     * @return              내 게시글에 대한 정보
     * @throws CustomException 사용자 식별자가 존재하지 않을 경우 예외 발생
     */
    @Override
    public PageResponse<PostResponseDto> findMyPost(Long loginId, Pageable pageable) {

        Page<Post> postPage = postRepository.findPostByUserIdOrElseThrow(loginId, pageable);

        Page<PostResponseDto> dtoPage = postPage.map(PostResponseDto::new);

        return new PageResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );    }

    /**
     * 나와 친구의 게시글 찾기
     *
     * @param loginId           사용자 식별자
     * @param pageable          페이징 정보
     * @return                  나와 친구들 게시글에 대한 정보
     * @throws CustomException  게시글이 존재하지 않을 경우 예외 발생
     */
    @Override
    public PageResponse<PostResponseDto> findRelatedPost(Long loginId, Pageable pageable) {

        List<Long> friendIds = friendRepository.findAllByUserIdAndStatusAccepted(loginId);

        friendIds.add(loginId);

        Page<Post> posts = postRepository.findPostByUserIdInOrElseThrow(friendIds, pageable);

        Page<PostResponseDto> dtoPage = posts.map(PostResponseDto::new);


        return new PageResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );

    }

    /**
     * 게시글 수정
     *
     * @param id                게시글 식별자
     * @param loginId           사용자 식별자
     * @param requestDto        수정하려는 게시글 정보
     * @throws CustomException  게시글이 존재하지 않을 경우 예외 발생
     * @throws CustomException  본인이 쓴 게시글이 아닐 경우 UNAUTHORIZED_ACCESS
     */
    @Transactional
    @Override
    public PostResponseDto updatePost(Long id, Long loginId, PostUpdateDto requestDto) {

        Post foundPost = postRepository.findPostByIdOrElseThrow(id);

        if(!foundPost.getUser().getId().equals(loginId)) {
            throw new CustomException(ExceptionCode.FORBIDDEN_ACCESS);
        }

        foundPost.updatePost(requestDto.getContents(), requestDto.getImageUrl());

        return new PostResponseDto(foundPost);
    }

    /**
     * 게시글 삭제
     *
     * @param id                게시글 식별자
     * @param loginId           사용자 식별자
     * @throws CustomException  게시글이 존재하지 않을 경우 예외 발생
     */
    @Override
    public void deletePost(Long id, Long loginId) {

        Post foundPost = postRepository.findPostByIdOrElseThrow(id);

        if(!foundPost.getUser().getId().equals(loginId)) {
            throw new CustomException(ExceptionCode.FORBIDDEN_ACCESS);
        }

        postRepository.delete(foundPost);
    }

    /**
     * user_id로 게시글 찾기
     *
     * @param id                user_id
     * @param pageable          페이징 정보
     * @return                  해당 게시글에 대한 정보
     * @throws CustomException  게시글이 존재하지 않을 경우 예외 발생
     */
    @Override
    public PageResponse<PostResponseDto> findPostByUserId(Long id, Pageable pageable) {

        Page<Post> postPage = postRepository.findPostByUserIdOrElseThrow(id, pageable);

        Page<PostResponseDto> dtoPage = postPage.map(PostResponseDto::new);

        return new PageResponse<>(
                dtoPage.getContent(),
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
        );
    }


}
