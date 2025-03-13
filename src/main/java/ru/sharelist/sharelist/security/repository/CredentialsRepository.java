package ru.sharelist.sharelist.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sharelist.sharelist.security.model.entity.Credentials;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, UUID> {
    Optional<Credentials> getCredentialsByLogin(String login);
}
