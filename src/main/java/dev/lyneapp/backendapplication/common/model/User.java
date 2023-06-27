package dev.lyneapp.backendapplication.common.model;


import dev.lyneapp.backendapplication.onboarding.model.Location;
import dev.lyneapp.backendapplication.onboarding.model.PhoneNumberLookUpData;
import dev.lyneapp.backendapplication.onboarding.model.Prompt;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    @Indexed(unique = true)
    private String userPhoneNumber;

    @NotBlank(message = "Verification code cannot be blank.")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must have exactly 6 digits.")
    @Size(min = 6, max = 6)
    private String verificationCode;

    @NotBlank(message = "password cannot be blank.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Password must have at least 8 and at most of 20 characters " +
                      "and contain at least one uppercase letter one lowercase letter, " +
                      "one number, and one special character.")
    @Size(min = 8, max = 20, message = "Password cannot be longer than 20 nor shorter than 8 characters.")
    @Getter(AccessLevel.NONE)
    private String password;
    private String passwordHash;
    private String passwordResetToken;

    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "Email must have the standard email pattern")
    @Indexed(unique = true)
    private String emailAddress;

    private String firstName;
    private String lastName;
    private String height;
    private String religion;
    private String job;
    private String industry;
    private String highestDegree;
    private String institutionName;
    private String videoUrl;
    private String aboutYou;
    private String role;
    private String gender;
    private String age;

    private double score;

    private List<String> tribe = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<String> mediaFileURLs = new ArrayList<>();
    private List<Prompt> prompts = new ArrayList<>();
    private List<String> blockedUsers = new ArrayList<>(); // A list of users blacked by their phone numbers

    private LocalDate dateOfBirth;

    private Location currentLocation;
    private Location placeOfBirthLocation;

    private LocalDateTime passwordCreatedDate;
    private LocalDateTime passwordUpdatedDate;

    private UserPreference preferences;
    private PhoneNumberLookUpData phoneNumberLookUpData;

    private boolean isBanned;
    private boolean accountEnabled;
    private boolean accountLocked;
    private boolean userIsVerified;
    private boolean doYouHaveChildren;
    private boolean doYouDrink;
    private boolean doYouSmoke;
    private boolean credentialsExpired;
    private boolean accountExpired;
    private boolean userLoggedIn;
    private boolean profileCreated;
    private boolean preferenceCreated;
    private boolean accountVerifiedBadge;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add authorities based on the user's role
        String role = getRole();
        switch (role) {
            case "admin" -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            case "premium" -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_PREMIUM_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            default -> authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() {
        return userPhoneNumber;
    }
    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
    @Override
    public boolean isEnabled() {
        return accountEnabled;
    }
}


