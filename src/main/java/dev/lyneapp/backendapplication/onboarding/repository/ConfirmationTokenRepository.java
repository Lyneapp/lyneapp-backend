package dev.lyneapp.backendapplication.onboarding.repository;


import dev.lyneapp.backendapplication.onboarding.model.ConfirmationToken;
import dev.lyneapp.backendapplication.common.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findByToken(String token);
    void deleteAllByUser(User user);
}
