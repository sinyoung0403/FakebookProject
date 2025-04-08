package com.example.fakebookproject.api.friend.repository;

import com.example.fakebookproject.api.friend.entity.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<FriendStatus, Long> {

    Page<FriendStatus> findById(Long userId, Pageable pageable);

    @Query("""
                    SELECT f, u FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR 
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    )
                    WHERE f.status = 1        
            """)
    Page<Object[]> findAllByUserIdAndStatusAcceptedOrElseThrow(@Param("userId") Long userId, Pageable pageable);

    @Query("""
                    SELECT f, u FROM FriendStatus f
                    JOIN User u ON (
                        (f.requestUser.id = :userId AND u.id = f.responseUser.id)
                        OR 
                        (f.responseUser.id = :userId AND u.id = f.requestUser.id)
                    )
                        
            """)
    Page<Object[]> findRecommendationAllByUserIdOrElseThrow(@Param("userId") Long userId, Pageable pageable);

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
