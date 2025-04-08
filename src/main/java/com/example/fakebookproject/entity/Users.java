package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class Users {

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 주소
    @Column(nullable = false, length = 30, unique = true)
    private String email;

    // 비밀번호 (Bcrypt 해시값 저장)
    @Column(nullable = false, length = 60)
    private String password;

    // 이름
    @Column(nullable = false, length = 10, name = "user_name")
    private String userName;

    // 생년월일
    @Column(nullable = false)
    private LocalDate birth;

    // 성별 (체크 제약조건)
    // M(남자), F(여자)
    @Column(nullable = false, columnDefinition = "CHAR(1) CHECK (gender IN ('M', 'F'))")
    private Character gender;

    // 휴대폰 번호
    @Column(nullable = false, length = 13)
    private String phone;

    // 이미지 주소
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl;

    // 취미
    @Column(length = 20)
    private String hobby;

    // 지역
    @Column(name = "city_name", length = 20)
    private String cityName;

    // 소프트 딜리트
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
