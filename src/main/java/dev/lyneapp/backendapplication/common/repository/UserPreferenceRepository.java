package dev.lyneapp.backendapplication.common.repository;


import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.onboarding.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends MongoRepository<UserPreference, String> {
    Optional<UserPreference> findByUserPhoneNumber(String userPhoneNumber);
}

