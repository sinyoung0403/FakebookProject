package com.example.fakebookproject.api.friend.repository;

import com.example.fakebookproject.api.friend.entity.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<FriendStatus, Long> {

    Page<FriendStatus> findById(Long userId, Pageable pageable);

    /**
     * 내 친구 목록
     * @param userId
     * @return
     */
    @Query("""
                    SELECT f, u FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR 
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    )
                    WHERE f.status = 1        
            """)
    List<Object[]> findAllByUserIdAndStatusAcceptedOrElseThrow(@Param("userId") Long userId);

    /**
     * 추천 친구 목록
     * @param userId
     * @param pageable
     * @return
     */
    @Query("""
                    SELECT f, u FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR 
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    ) 
                    WHERE f.status = 0 AND u.cityName = :cityName
                        
            """)
    Page<Object[]> findRecommendationAllByUserIdOrElseThrow(@Param("userId") Long userId, Pageable pageable);

    /**
     * 요청 받은 친구 목록
     * @param userId
     * @param pageable
     * @return
     */
    @Query("""
                    SELECT f, u FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR 
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    )
                        
            """)
    Page<Object[]> findReceivedAllByUserIdOrElseThrow(@Param("userId") Long userId, Pageable pageable);
}
