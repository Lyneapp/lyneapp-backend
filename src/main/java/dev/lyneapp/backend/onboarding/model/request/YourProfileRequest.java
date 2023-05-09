package dev.lyneapp.backend.onboarding.model.request;

import dev.lyneapp.backend.onboarding.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class YourProfileRequest {

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Enter a valid phone number")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters.")
    private String yourPhoneNumber;

    @NotBlank(message = "Name cannot be blank.")
    private Name yourName;

    @NotBlank(message = "email cannot be blank.")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "Email must have the standard email pattern")
    private String yourEmailAddress;

    @NotBlank(message = "date of birth cannot be blank.")
    private LocalDate yourDateOfBirth;

    @NotBlank(message = "location cannot be blank.")
    private Location yourCurrentLocation;

    @NotBlank(message = "location cannot be blank.")
    private Location yourPlaceOfBirthLocation;

    @NotBlank(message = "gender cannot be blank.")
    private String yourGender;

    private String yourHeight;
    private List<String> yourTribe;
    private boolean doYouDrink; //TODO for preference we'll use "shouldTheySmoke" and their* as prefix
    private boolean doYouSmoke;
    private String yourReligion;
    private boolean doYouHaveChildren;
    private Occupation yourOccupation;
    private Qualification yourQualification;
    private List<String> yourLanguages;
    private List<String> yourInterests;

    @Size(max = 128)
    private String aboutYou;

    @Size(max = 3, message = "The number of prompts should be at most 3.")
    private List<@NotBlank(message = "Answer cannot be blank.") @Size(max = 128, message = "Answer cannot be more than 128 characters.") Prompt> prompts;
}
