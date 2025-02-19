package ru.sharelist.sharelist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sharelist.sharelist.model.JwtRequestDto;
import ru.sharelist.sharelist.model.JwtResponseDto;
import ru.sharelist.sharelist.model.RefreshJwtRequestDto;
import ru.sharelist.sharelist.service.AuthService;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JwtRequestDto authRequest) throws Exception {
        final JwtResponseDto token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> getNewAccessToken(@RequestBody RefreshJwtRequestDto request) throws Exception {
        final JwtResponseDto token = authService.getAccessToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> getNewRefreshToken(@RequestBody RefreshJwtRequestDto request) throws Exception {
        final JwtResponseDto token = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(token);
    }
}
