package dev.lyneapp.backendapplication.common.repository;


import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.onboarding.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepository extends MongoRepository<UserPreference, String> {
    List<UserPreference> findUsersByPreferredGenderAndPreferredTribesAndPreferredReligionAndShouldTheyHaveChildrenAndShouldTheyDrinkAndShouldTheySmoke(
            Gender preferredGender,
            Tribe preferredTribes,
            Religion preferredReligion,
            boolean shouldTheyHaveChildren, boolean shouldTheyDrink, boolean shouldTheySmoke);
}

