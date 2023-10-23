package dev.lyneapp.backendapplication.onboarding.model.request;


import dev.lyneapp.backendapplication.onboarding.model.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO When user selects OpenToAll or Both - The other option needs to be greyed out. They need to be mutually exclusive choices

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceDTO {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String userPhoneNumber;

    private Gender preferredGender;
    private Tribe preferredTribes;
    private AgeRange preferredAgeRange;
    private HeightRange preferredHeightRange;
    private Religion preferredReligion;
    private Children doYouWantSomeoneWithChildren;
    private Qualification preferredQualification;
    private Occupation preferredOccupation;

    private List<Location> preferredLocations = new ArrayList<>();
    private List<String> preferredInterests = new ArrayList<>();
    private List<String> preferredLanguages = new ArrayList<>();

    private LocalDateTime preferenceCreatedAt;
    private LocalDateTime preferenceUpdatedAt;

    private boolean isAMatch;
    private boolean isBlocked;
    private boolean shouldTheyDrink;
    private boolean shouldTheySmoke;
    private boolean shouldTheyHaveChildren;
    private boolean preferenceCreated;
    private boolean preferenceUpdated;
}
