package dev.lyneapp.backendapplication.onboarding.model.request;


import dev.lyneapp.backendapplication.onboarding.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

// TODO for preference we'll use "shouldTheySmoke" and their* as prefix

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Enter a valid phone number")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters.")
    private String userPhoneNumber;

    @NotBlank(message = "Name cannot be blank.")
    private Name name; //has firstName and lastName

    @NotBlank(message = "date of birth cannot be blank.")
    private LocalDate dateOfBirth;//has LocalDate object

    @NotBlank(message = "location cannot be blank.")
    private Location currentLocation; //has latitude, longitude, city and country

    @NotBlank(message = "location cannot be blank.")
    private Location placeOfBirthLocation; //has latitude, longitude, city and country

    @NotBlank(message = "gender cannot be blank.")
    private String gender;

    private String role;
    private String religion;
    private String height; // TODO get height in cm or collect height in inches and convert to cm and save in database

    @Size(max = 128)
    private String aboutYou;

    private boolean doYouDrink;
    private boolean doYouSmoke;
    private boolean doYouHaveChildren;

    private Occupation occupation; //has job and industry
    private Qualification qualification; //has highestDegree and institutionName

    private List<String> tribe; // A list of tribes
    private List<String> languages;
    private List<String> interests;

    @Size(max = 3, message = "The number of prompts should be at most 3.")
    private List<@NotBlank(message = "Answer cannot be blank.") @Size(max = 128, message = "Answer cannot be more than 128 characters.") Prompt> prompts;
}
