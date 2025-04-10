package com.example.fakebookproject.common.config;

import com.example.fakebookproject.api.comment.entity.Comment;
import com.example.fakebookproject.api.comment.repository.CommentRepository;
import com.example.fakebookproject.api.post.entity.Post;
import com.example.fakebookproject.api.post.repository.PostRepository;
import com.example.fakebookproject.api.user.entity.User;
import com.example.fakebookproject.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

        List<User> users = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        // 사용자 생성
        for (int i = 1; i <= 5; i++) {
            String name = "test" + i;
            String email = "test" + i + "@test.com";
            String rawPassword = "pwdpwdpwd1234!";
            LocalDate birth = LocalDate.parse("1999-05-04"); // 수정: yyyy-MM-dd 형식
            String gender = "M";
            String phone = "010-0000-0000";

            String encodedPassword = passwordEncoder.encode(rawPassword);

            User user = new User(email, encodedPassword, name, birth, gender, phone);
            userRepository.save(user);
            users.add(user);
        }

        // 게시물 생성
        for (User user : users) {
            for (int j = 1; j <= 2; j++) {
                Post post = new Post("게시물 " + j + " for " + user.getEmail(), "할일", user);
                postRepository.save(post);
                posts.add(post);
            }
        }

        // 댓글 생성 (자기 글이 아닌 게시글에 다른 유저들이 댓글 작성)
        for (Post post : posts) {
            for (User user : users) {
                Comment comment = new Comment(user, post, user.getUserName() + "의 댓글 on " + post.getContents());
                commentRepository.save(comment);
            }
        }

        log.info("초기 데이터 생성완료");
    }

}
