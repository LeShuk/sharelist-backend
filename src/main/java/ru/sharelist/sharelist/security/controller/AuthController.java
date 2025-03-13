package ru.sharelist.sharelist.security.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sharelist.sharelist.security.exception.InvalidJWTTokenException;
import ru.sharelist.sharelist.security.model.dto.JwtRequestDto;
import ru.sharelist.sharelist.security.model.dto.JwtResponseDto;
import ru.sharelist.sharelist.security.model.dto.RefreshJwtRequestDto;
import ru.sharelist.sharelist.security.service.AuthService;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto login(@RequestBody @Valid JwtRequestDto authRequest,
                                HttpServletResponse response) {
        return authService.login(authRequest, response);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseDto refresh(@RequestBody @Valid RefreshJwtRequestDto request, HttpServletResponse response)
            throws InvalidJWTTokenException {
        return authService.refresh(request, response);
    }
}
