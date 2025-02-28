package ru.sharelist.sharelist.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.exception.CustomBadCredentialsException;
import ru.sharelist.sharelist.security.model.Credentials;
import ru.sharelist.sharelist.security.repository.CredentialsRepository;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialsRepository credentialsRepository;

    public Credentials getByLogin(String login) {
        return credentialsRepository.getCredentialsByLogin(login)
                .stream()
                .findFirst()
                .orElseThrow(CustomBadCredentialsException::new);
    }

    public Credentials getByLoginAndPassword(String login, String password) {
        return credentialsRepository.getCredentialsByLoginAndPassword(login, password)
                .stream()
                .findFirst()
                .orElseThrow(CustomBadCredentialsException::new);
    }
}
