package ru.sharelist.sharelist.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.exception.CustomBadCredentialsException;
import ru.sharelist.sharelist.security.model.entity.Credentials;
import ru.sharelist.sharelist.security.repository.CredentialsRepository;
import ru.sharelist.sharelist.security.util.PasswordUtil;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialsRepository credentialsRepository;

    public Credentials getByLogin(String login) {
        return credentialsRepository.getCredentialsByLogin(login)
                .orElseThrow(CustomBadCredentialsException::new);
    }

    public Credentials getByLoginAndPassword(String login, String password) {
        Credentials credentials = getByLogin(login);
        if (PasswordUtil.matches(password, credentials.getPassword())) {
            return credentials;
        }
        throw new CustomBadCredentialsException();
    }
}
