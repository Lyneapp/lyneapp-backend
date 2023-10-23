package dev.lyneapp.backendapplication.common.model;


import dev.lyneapp.backendapplication.onboarding.model.Location;
import dev.lyneapp.backendapplication.onboarding.model.Prompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

// TODO Implement share profile feature
// TODO Implement Report user feature
// TODO Implement Bouquet meter feature


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String id;
    private String userPhoneNumber;
    private String firstName;
    private String lastName;
    private String height;
    private String religion;
    private String job;
    private String industry;
    private String highestDegree;
    private String institutionName;
    private String aboutUser;
    private String gender;
    private String age;
    private String dateOfBirth;
    private Location placeOfBirth;
    private String profileUrl;
    private int remainingBouquets;

    private List<String> tribe = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<String> mediaFileURLs = new ArrayList<>();
    private List<Prompt> prompts = new ArrayList<>();

    private Location currentLocation;

    private boolean doYouHaveChildren;
    private boolean doYouDrink;
    private boolean doYouSmoke;
    private boolean accountVerifiedBadge;
}


