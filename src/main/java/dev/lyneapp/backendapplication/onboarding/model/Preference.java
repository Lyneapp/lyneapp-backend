package dev.lyneapp.backendapplication.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class Preference {
    private String userPhoneNumber;
    private Gender preferredGender;
    private Tribe preferredTribes;
    private AgeRange preferredAgeRange;
    private HeightRange preferredHeightRange;
    private Religion preferredReligion;
    private Children doYouWantSomeoneWithChildren;
    private Qualification preferredQualification;

    private List<Location> preferredLocations = new ArrayList<>();
    private List<String> preferredInterests = new ArrayList<>();
    private List<String> preferredLanguages = new ArrayList<>();

    private LocalDateTime preferenceCreatedAt;

    private boolean isAMatch;
    private boolean isBlocked;
    private boolean shouldTheyDrink;
    private boolean shouldTheySmoke;
    private boolean preferenceCreated;

    public Preference(Gender preferredGender, Tribe preferredTribes, AgeRange preferredAgeRange, HeightRange preferredHeightRange,
                      Religion preferredReligion, Children doYouWantSomeoneWithChildren, Qualification preferredQualification,
                      List<Location> preferredLocations, List<String> preferredInterests, List<String> preferredLanguages,
                      boolean isAMatch, boolean isBlocked, boolean shouldTheyDrink, boolean shouldTheySmoke) {
        this.preferredGender = preferredGender;
        this.preferredTribes = preferredTribes;
        this.preferredAgeRange = preferredAgeRange;
        this.preferredHeightRange = preferredHeightRange;
        this.preferredReligion = preferredReligion;
        this.doYouWantSomeoneWithChildren = doYouWantSomeoneWithChildren;
        this.preferredQualification = preferredQualification;
        this.preferredLocations = preferredLocations;
        this.preferredInterests = preferredInterests;
        this.preferredLanguages = preferredLanguages;
        this.isAMatch = isAMatch;
        this.isBlocked = isBlocked;
        this.shouldTheyDrink = shouldTheyDrink;
        this.shouldTheySmoke = shouldTheySmoke;

    }
}


