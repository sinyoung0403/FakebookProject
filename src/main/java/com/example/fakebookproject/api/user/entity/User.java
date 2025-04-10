package com.example.fakebookproject.api.user.entity;

import com.example.fakebookproject.common.config.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "users")
// 엔티티 삭제 시 실제로는 삭제하지 않고 is_deleted를 true로 업데이트
@SQLDelete(sql = "update users set is_deleted = true where id = ?")
public class User extends BaseTimeEntity {

    private static final String DEFAULT_IMAGE_URL = "https://cdn-icons-png.flaticon.com/512/847/847969.png";

    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일 주소
    @Column(nullable = false, length = 50, unique = true)
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
    private String gender;

    // 휴대폰 번호
    @Column(nullable = false, length = 13)
    private String phone;

    // 이미지 주소
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageUrl = DEFAULT_IMAGE_URL;

    // 취미
    @Column(length = 50)
    private String hobby;

    // 지역 "경기도, 강원도, 충청도, 전라도, 경상도, 제주특별자치도 등등"
    @Column(name = "city_name",
            columnDefinition = "VARCHAR(6) CHECK (city_name IN ('서울', '인천', '대전', '대구', '부산', '광주', '경기'," +
                    " '강원', '충청', '전라', '경상', '제주'))")
    private String cityName;

    // 소프트 딜리트
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public User() {
        this.isDeleted = false;
    }

    public User(String email, String password, String userName, LocalDate birth, String gender, String phone) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.birth = birth;
        this.gender = gender;
        this.phone = phone;
        this.isDeleted = false;
    }

    public void updateUser(String userName, LocalDate birth, String phone,
                           String imageUrl, String hobby, String cityName) {
        if (userName != null) this.userName = userName;
        if (birth != null) this.birth = birth;
        if (phone != null) this.phone = phone;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (hobby != null) this.hobby = hobby;
        if (cityName != null) this.cityName = cityName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}