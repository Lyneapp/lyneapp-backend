package dev.lyneapp.backendapplication.onboarding.repository;


import dev.lyneapp.backendapplication.onboarding.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserPhoneNumber(String phoneNumber);
    Optional<User> findByPasswordResetToken(String token);
    @NotNull
    Optional<User> findById(@NotNull String id);
}
