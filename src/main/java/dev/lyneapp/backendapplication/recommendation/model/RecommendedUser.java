
package dev.lyneapp.backendapplication.recommendation.model;


import dev.lyneapp.backendapplication.onboarding.model.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO Implement share profile feature
// TODO Implement Report user feature
// TODO Implement Bouquet meter feature

// TODO time since last interaction feature (time past since this user was recommended to you and you swiped left or disliked)
// TODO time since last interaction feature (time past since this user was recommended to you and you swiped right or liked)


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedUser {
    private String userId; // User getting the recommendation
    private String recommendationId;
    private String recommendedUserId;
    private String recommendationWeight; // TODO Set a default value for this. A score that can be used to determine how likely this user is to be recommended to you.

    private Gender preferredGender;
    private Tribe preferredTribes;
    private AgeRange preferredAgeRange;
    private HeightRange preferredHeightRange;
    private Religion preferredReligion;
    private Qualification preferredQualification;
    private Occupation preferredOccupation;

    private List<Location> preferredLocations = new ArrayList<>();
    private List<String> preferredInterests = new ArrayList<>();
    private List<String> preferredLanguages = new ArrayList<>();

    private Duration timeSinceDislikedInteraction;
    private Duration timeSinceLikedInteraction;
    private Duration timeSinceViewedProfile;

    private LocalDateTime recommendationCreatedAt;

    private boolean shouldTheyDrink;
    private boolean shouldTheySmoke;
    private boolean shouldTheyHaveChildren;

    private boolean liked; // true if the user liked and false if the user did not like
    private boolean isAMatch;
    private boolean isBanned;
    private boolean isBlocked;
    private boolean viewedProfile;
    private boolean interactedWith;
    private boolean accountVerifiedBadge;

}


