package com.example.fakebookproject.api.post.repository;

import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    default Page<Post> findPostByUserIdOrElseThrow(Long userId, Pageable pageable){
        Page<Post> page = findByUserId(userId, pageable);
        if(page.isEmpty()){
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

    default void validateExistenceByPost_Id(Long planId) {
        if (!existsById(planId)) {
            throw new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE);
        }
    }

    default Post findPostByIdOrElseThrow(Long id){
        return findById(id).orElseThrow(()->
                new CustomException(ExceptionCode.NOT_FOUND_SCHEDULE
                )
        );
    }


    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
    void increaseLikeCount(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId")
    void decreaseLikeCount(@Param("postId") Long postId);


    Page<Post> findByUserId(Long userId, Pageable pageable);

    Page<Post> findPostByUserIdIn(List<Long> userIds, Pageable pageable);

}
