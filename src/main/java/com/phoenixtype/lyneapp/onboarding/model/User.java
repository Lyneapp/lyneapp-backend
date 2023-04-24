package com.phoenixtype.lyneapp.onboarding.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    @Indexed(unique = true)
    private String phoneNumber;

    @NotBlank(message = "Verification code cannot be blank.")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must have exactly 6 digits.")
    @Size(min = 6, max = 6)
    private String verificationCode;

    @NotBlank(message = "password cannot be blank.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Password must have at least 8 and at most of 20 characters and contain at least one uppercase letter, " +
                     "one lowercase letter, one number, and one special character.")
    @Size(min = 8, max = 20, message = "Password cannot be longer than 20 nor shorter than 8 characters.")
    @Getter(AccessLevel.NONE)
    private String password;

    //TODO User? = "user", Premium-User? = "premium", Admin? = "admin"
    private String role;
    private String passwordHash;

    private String firstName;
    private String lastName;

    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "Email must have the standard email pattern")
    private String emailAddress;

    private String currentCity;
    private String currentCountry;
    private String placeOfBirthCity;
    private String placeOfBirthCountry;
    private String gender;
    private String height;
    private String tribe;
    private String religion;
    private String occupation;
    private String industry;
    private String highestEducationLevel;
    private String institutionName;
    private String videoUrl;
    private String aboutMe;

    private List<String> languages;
    private List<String> interests;
    private List<String> photoUrls;

    private LocalDate dateOfBirth;

    private boolean accountEnabled;
    private boolean doYouHaveChildren;
    private boolean doYouDrink;
    private boolean doYouSmoke;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;
}


