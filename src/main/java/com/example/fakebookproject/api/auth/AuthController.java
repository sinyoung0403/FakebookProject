package com.example.fakebookproject.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Access Token 재발급
    @PostMapping("/api/auth/reissue")
    public ResponseEntity<TokenResponseDto> reissue(
            @RequestHeader("REFRESH_TOKEN") String refreshToken
    ) {
        TokenResponseDto newToken = authService.reissue(refreshToken);
        return ResponseEntity.ok(newToken);
    }
}
