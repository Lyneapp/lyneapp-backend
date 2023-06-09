package dev.lyneapp.backendapplication.onboarding.repository;


import dev.lyneapp.backendapplication.onboarding.model.Token;
import dev.lyneapp.backendapplication.common.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends MongoRepository<Token, String> {
    List<Token> findAllValidTokensByUser(User user);
    Optional<Token> findByToken(String token);
    void deleteAllByUser(User user);
}
