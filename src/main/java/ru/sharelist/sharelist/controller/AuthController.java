package ru.sharelist.sharelist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sharelist.sharelist.exception.CustomBadCredentialsException;
import ru.sharelist.sharelist.exception.InvalidJWTTokenException;
import ru.sharelist.sharelist.model.dto.JwtRequestDto;
import ru.sharelist.sharelist.model.dto.JwtResponseDto;
import ru.sharelist.sharelist.model.dto.RefreshJwtRequestDto;
import ru.sharelist.sharelist.service.AuthService;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto login(@RequestBody JwtRequestDto authRequest) throws CustomBadCredentialsException {
        return authService.login(authRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto refresh(@RequestBody RefreshJwtRequestDto request)
            throws CustomBadCredentialsException, InvalidJWTTokenException {
        return authService.refresh(request.refreshToken());
    }
}
