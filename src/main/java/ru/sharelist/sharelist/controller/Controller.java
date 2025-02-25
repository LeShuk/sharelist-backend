package ru.sharelist.sharelist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.sharelist.sharelist.model.JwtAuthentication;
import ru.sharelist.sharelist.service.AuthService;

//todo удалить контроллер

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    private final AuthService authService;

    /**
     * Приветствие для зарегистрированного пользователя
     */
    @GetMapping("/hello/user")
    @ResponseStatus(HttpStatus.OK)
    public String helloUser() {
        final JwtAuthentication authInfo = authService.getAuthInfo();
        return "Hello user " + authInfo.getPrincipal() + "!";
    }
}
