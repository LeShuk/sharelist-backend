package ru.sharelist.sharelist.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sharelist.sharelist.security.model.Credentials;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final List<Credentials> credentials;

    public CredentialService() {
        //fixme получать credentials из БД
        credentials = List.of(
                new Credentials("katya", "123"),
                new Credentials("lyosha", "456")
        );
    }

    public Optional<Credentials> getByLogin(String login) {
        return credentials.stream()
                .filter(credential -> Objects.equals(credential.login(), login))
                .findFirst();
    }
}
