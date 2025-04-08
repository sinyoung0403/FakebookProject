package com.example.fakebookproject.api.post.repository;

import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    default Page<Post> findPostByUserIdOrElseThrow(Long userId, Pageable pageable) {
        Page<Post> page = findByUserId(userId, pageable);
        if (page.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
        return page;
    }

    default Page<Post> findPostByUserIdInOrElseThrow(List<Long> userIds, Pageable pageable) {
        Page<Post> page = findPostByUserIdIn(userIds, pageable);
        if (page.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_USER);
        }
        return page;
    }

    /* Post 가 존재하는지 확인 */
    boolean existsById(Long planId);

    default void validateNotExistenceByPost_Id(Long planId) {
        if (!existsById(planId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_POST);
        }
    }

    default Post findPostByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new CustomException(ExceptionCode.NOT_FOUND_POST
                )
        );
    }


    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findPostByUserIdIn(List<Long> userIds, Pageable pageable);

}
