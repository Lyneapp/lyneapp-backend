package dev.lyneapp.backend.onboarding.repository;

import dev.lyneapp.backend.onboarding.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByPhoneNumberVerificationCode(String verificationCode);
    boolean existsByPhoneNumber(String phoneNumber);
}
