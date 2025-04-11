package com.example.fakebookproject.common.util;

import com.example.fakebookproject.api.user.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenUtils {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.refresh-key}")
    private String REFRESH_KEY;

    private final String DATA_KEY = "userId";

    /**
     * Access Token 생성
     *
     * @param user
     * @return
     */
    public String createJwtToken(User user) {
        return Jwts.builder().setSubject(String.valueOf(user.getId())) // 토큰 주제 설정 (보통 고유 식별자) | Long 일 경우, String 으로 변환.
                .setHeader(createHeader()) // Header 추가
                .setClaims(createClaims(user)) // Payload 설정. 사용자 정보 넣기
                .setExpiration(createExpireDate(1000 * 60 * 5)) // 만료 시간 설정 (5분)
                .signWith(SignatureAlgorithm.HS256, createSigningKey(SECRET_KEY)) // Signature 설정 . 무결성 검증 역할 | 알고리즘 + 시크릿 키
                .compact();
    }

    /**
     * Refresh Token 저장
     *
     * @param user
     * @return
     */
    public String saveRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setHeader(createHeader()).setClaims(createClaims(user))
                .setExpiration(createExpireDate(1000 * 60 * 30)) // 만료 시간 설정 (10분)
                .signWith(SignatureAlgorithm.HS256, createSigningKey(REFRESH_KEY)).compact();
    }


    /**
     * JWT Access 토큰 유효성 검사
     *
     * @param token
     * @return 정상일 시 true, 유효하지 않을 시 false
     */
    public boolean isValidToken(String token) {
        log.info("isValidToken: {}", token);
        try {
            Claims accessClaims = getClaimsFromToken(token);
            log.info("Access expireTime: {}", accessClaims.getExpiration());
            log.info("Access userId: {}", accessClaims.get(DATA_KEY));
            return true;
        } catch (ExpiredJwtException exception) { // Token 만료
            log.info("Token Expired UserID: {}", exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            log.info("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.info("Token Null");
            return false;
        }
    }

    /**
     * JWT Refresh Token 유효성 검사
     *
     * @param token
     * @return 정상일 시 true, 유효하지 않을 시 false
     */
    public boolean isValidRefreshToken(String token) {
        try {
            Claims refreshClaims = getClaimsToken(token);
            log.info("Refresh expireTime: {}", refreshClaims.getExpiration());
            log.info("Refresh userId: {}", refreshClaims.get(DATA_KEY));
            return true;
        } catch (ExpiredJwtException exception) { // Token 만료
            log.info("Token Expired UserID: {}", exception.getClaims().getSubject());
            return false;
        } catch (JwtException exception) {
            log.info("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.info("Token Null");
            return false;
        }
    }

    /**
     * JWT 토큰의 만료 시간(Expiration Time) 만들기
     *
     * @param expireDate : 시간 연산에 사용되는 밀리초 값 = long
     * @return
     */
    private Date createExpireDate(long expireDate) {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expireDate); // 현재시간 + 만료 시간
    }

    /**
     * Token 의 Header 생성
     *
     * @return header
     */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "ACCESS_TOKEN");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }


    /**
     * Token 의 Payload 생성
     *
     * @param user
     * @return
     */
    private Map<String, Object> createClaims(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(DATA_KEY, user.getId());
        claims.put("userName", user.getUserName());
        claims.put("userEmail", user.getEmail());

        return claims;
    }

    /**
     * JWT 서명을 위한 비밀 키 생성 매서드
     *
     * @param key
     * @return
     */
    private Key createSigningKey(String key) {
        return new SecretKeySpec(Base64.getDecoder().decode(key), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Access Token 의 Payload (Claims) 를 꺼내는 메서드
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(createSigningKey(SECRET_KEY))  // 수정
                .parseClaimsJws(token).getBody();
    }

    /**
     * Refresh Token 의 Payload (Claims) 를 꺼내는 메서드
     *
     * @param token
     * @return
     */
    private Claims getClaimsToken(String token) {
        return Jwts.parser()
                .setSigningKey(createSigningKey(REFRESH_KEY))
                .parseClaimsJws(token).getBody();
    }


    /**
     * Access Token 에서 UserId 꺼내는 멧드
     * @param token
     * @return
     */
    public Long getUserIdFromAccessToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();

        return claims.get(DATA_KEY, Long.class);
    }

    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(REFRESH_KEY))
                .parseClaimsJws(token)
                .getBody();

        return claims.get(DATA_KEY, Long.class);
    }
}
