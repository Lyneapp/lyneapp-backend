package dev.lyneapp.backendapplication.common.repository;


import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.onboarding.model.Gender;
import dev.lyneapp.backendapplication.onboarding.model.Religion;
import dev.lyneapp.backendapplication.onboarding.model.Tribe;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserPhoneNumber(String phoneNumber);
    Optional<User> findByPasswordResetToken(String token);
    @NotNull
    Optional<User> findById(@NotNull String id);
    List<User> findUsersByGenderAndTribeAndReligionAndDoYouHaveChildrenAndDoYouDrinkAndDoYouSmoke(
            String gender,
            List<String> tribe,
            String religion,
            boolean doYouHaveChildren, boolean doYouDrink, boolean doYouSmoke);
}
