package com.example.fakebookproject.common.auth;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class Base64Encoder {

    // 생성된 Base64 인코딩 키를 저장할 파일 경로
    private static final String FILE_PATH = "src/main/resources/application-jwt.properties";

    public static void main(String[] args) {
        // 원본 시크릿 키 (JWT 서명에 사용될 문자열)
        String rawKey = "custom-super-super-secret-key-made-by-kim-tae-ik";

        // Base64로 인코딩하여 application-jwt.properties에 넣을 수 있는 형태로 변환
        String encodedKey = Base64.getEncoder().encodeToString(rawKey.getBytes());

        // 로그로 인코딩된 키 출력
        log.info("Encoded Key: {}", encodedKey);

        // 지정된 파일에 jwt.secret=인코딩된 키 형태로 저장
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write("jwt.secret=" + encodedKey + "\n");
            System.out.println("Encoded key has been written to application-jwt.properties");
        } catch (IOException e) {
            // 파일 쓰기 실패 시 오류 출력
            System.err.println("Failed to write to file: " + e.getMessage());
        }

    }
}
