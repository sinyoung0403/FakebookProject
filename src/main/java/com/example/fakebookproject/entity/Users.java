package com.example.fakebookproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Check(constraints = "gender IN ('M', 'W') AND deleted IN ('Y', 'N')")
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(length = 1, nullable = false)
    private String gender;

    @Column(nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String hobby;
    private String area;

    @Column(length = 1, nullable = false)
    private String deleted;
}
