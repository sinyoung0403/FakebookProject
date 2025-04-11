package com.example.fakebookproject.common.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;             // 서명에 사용할 비밀 키
    private final JwtParser jwtParser; // JWT 파서 (검증에 사용)

    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 5; // Access Token 유효 시간: 5분 (밀리초 단위)
    private final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 15; // Refresh Token 유효 시간: 15분 (밀리초 단위)

    /**
     * 생성자에서 시크릿 키를 디코딩해 HMAC 키 생성,
     * JWT 파서를 초기화해 토큰 서명 검증에 사용
     */
    public JwtProvider(@Value("${jwt.secret}") String secret) {
            this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            this.jwtParser = Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build();
    }

    /**
     * Access Token 생성 (사용자 ID를 subject로 포함)
     *
     * @param userId 사용자 고유 ID
     * @return Access Token (5분 유효)
     */
    public String createAccessToken(Long userId) {
        return buildToken(userId.toString(), ACCESS_TOKEN_VALIDITY);
    }

    /**
     * Refresh Token 생성
     *
     * @param userId 사용자 고유 ID
     * @return Refresh Token (15분 유효)
     */
    public String createRefreshToken(Long userId) {
        return buildToken(userId.toString(), REFRESH_TOKEN_VALIDITY);
    }

    /**
     * JWT 공통 생성 로직
     *
     * @param subject JWT 에 담길 사용자 식별값
     * @param validityInMillis 토큰 유효 시간 (밀리초)
     * @return 생성된 JWT 문자열
     *
     * - setIssuedAt: 토큰 발급 시간 (iat 클레임)
     * - setExpiration: 토큰 만료 시간 (exp 클레임)
     */
    private String buildToken(String subject, long validityInMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setSubject(subject)         // 사용자 식별자 (userId)
                .setIssuedAt(now)            // 발급 시간
                .setExpiration(expiry)       // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * JWT 유효성 검사
     *
     * @param token 검증할 JWT
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);  // 토큰 파싱
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * JWT에서 사용자 ID 추출
     *
     * @param token JWT 문자열
     * @return subject에 저장된 사용자 ID
     */
    public Long getUserId(String token) {
        String userId = jwtParser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Long.parseLong(userId);
    }

    /**
     * 남은 유효 시간(ms) 반환
     * (만료 시점 - 현재 시점)
     */
    public long getExpiration(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

}
