package com.example.fakebookproject.api.friend.repository;

import com.example.fakebookproject.api.friend.entity.FriendStatus;
import com.example.fakebookproject.common.exception.CustomException;
import com.example.fakebookproject.common.exception.ExceptionCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendStatus, Long> {

    /**
     * 친구 요청 수락 위한 조회
     * @param requestUserId
     * @param responseUserId
     * @return
     */
    Optional<FriendStatus> findFriendStatusByRequestUserIdAndResponseUserId(Long requestUserId, Long responseUserId);


    /**
     * 친구 요청 테이블 null 예외처리
     * @param requestUserId
     * @param responseUserId
     * @return
     */
    default FriendStatus findByRequestUserIdAndResponseUserIdOrElseThrow(Long requestUserId, Long responseUserId) {
        return findFriendStatusByRequestUserIdAndResponseUserId(requestUserId, responseUserId)
                .orElseThrow(() -> new CustomException(ExceptionCode.FRIEND_REQUEST_NOT_FOUND));
    }


    /**
     * 중복 요청 확인
     * @param requestUserId
     * @param responseUserId
     * @return
     */
    @Query("""
                    SELECT f FROM FriendStatus f
                    WHERE
                    (f.requestUser.id = :requestUserId AND f.responseUser.id = :responseUserId)
                    OR
                    (f.requestUser.id = :responseUserId AND f.responseUser.id = :requestUserId)

            """)
    Optional<FriendStatus> findFriendStatusByRequestUserIdAndResponseUserIdOrResponseUserIdAndRequestUserId(@Param("requestUserId") Long requestUserId, @Param("responseUserId") Long responseUserId);

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
     * @param cityName
     * @param hobby
     * @return
     */
    @Query("""
                    SELECT u.id
                    FROM User u
                    WHERE u.id <> :userId
                    AND (u.cityName = :cityName OR u.hobby = :hobby)
                    AND u.id NOT IN (
                        SELECT CASE
                                 WHEN f.requestUser.id = :userId THEN f.responseUser.id
                                 WHEN f.responseUser.id = :userId THEN f.requestUser.id
                               END
                        FROM FriendStatus f
                        WHERE f.requestUser.id = :userId OR f.responseUser.id = :userId
                    )

            """)
    List<Long> findRecommendationAllByUserIdOrElseThrow(@Param("userId") Long userId, @Param("cityName") String cityName, @Param("hobby") String hobby);

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

    @Query("""
                SELECT CASE
                            WHEN f.requestUser.id = :userId THEN f.responseUser.id 
                            ELSE f.requestUser.id 
                       END
                FROM FriendStatus f
                WHERE (f.requestUser.id = :userId OR f.responseUser.id = :userId)
                  AND f.status = 1
            """)
    List<Long> findAllByUserIdAndStatusAccepted(@Param("userId") Long userId);

    @Modifying
    @Query("""
                DELETE 
                FROM FriendStatus f
                WHERE f.requestUser.id = :requestUserId 
                AND f.responseUser.id = :responseUserId
            """)
    void deleteByRequestUserIdAndResponseUserId(@Param("requestUserId") Long requestUserId, @Param("responseUserId") Long responseUserId);

}
