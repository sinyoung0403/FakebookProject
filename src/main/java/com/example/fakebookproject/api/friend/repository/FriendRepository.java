package com.example.fakebookproject.api.friend.repository;

import com.example.fakebookproject.api.friend.entity.FriendStatus;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FriendRepository extends JpaRepository<FriendStatus, Long> {

    Page<FriendStatus> findById(Long userId, Pageable pageable);

    default FriendStatus findFriendStatusByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new CustomException(ExceptionCode.NOT_FOUND_COMMENT)
        );
    }

    /**
     * 내 친구 목록
     * @param userId
     * @return
     */
    @Query("""
                    SELECT CASE
                           WHEN f.requestUser.id = :userId THEN f.responseUser.id
                           ELSE f.requestUser.id
                           END
                    FROM FriendStatus f
                    WHERE (f.requestUser.id = :userId OR f.responseUser.id = :userId)
                    AND f.status = 1      
            """)
    List<Long> findAllByUserIdAndStatusAcceptedOrElseThrow(@Param("userId") Long userId);

    /**
     * 추천 친구 목록
     * @param userId
     * @param cityName
     * @return
     */
    @Query("""
                    SELECT u.id FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    )
                    WHERE f.status = 0 AND u.cityName = :cityName

            """)
    List<Long> findRecommendationAllByUserIdOrElseThrow(@Param("userId") Long userId, @Param("cityName") String cityName);

    /**
     * 내가 요청 받은 친구 목록
     * @param userId
     * @return
     */
    @Query("""
                    SELECT f.requestUser.id 
                    FROM FriendStatus f
                    WHERE f.responseUser.id = :userId
                    AND f.status = 0
                    AND f.requestUser.id <> :userId

            """)
    List<Long> findReceivedAllByUserIdOrElseThrow(@Param("userId") Long userId);

    /**
     * 내가 요청한 친구 목록
     * @param userId
     * @return
     */
    @Query("""
                    SELECT f.responseUser.id 
                    FROM FriendStatus f
                    WHERE f.requestUser.id = :userId
                    AND f.status = 0
                    AND f.responseUser.id <> :userId

            """)
    List<Long> findSentAllByUserIdOrElseThrow(@Param("userId") Long userId);
}
