package dev.lyneapp.backend.preferences.model.request;


import dev.lyneapp.backend.onboarding.model.Location;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**DOB stands for Date Of Birth */
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class TheirPlaceOfBirthRequest {

    @NotBlank(message = "city cannot be blank.")
    private String city;
    @NotBlank(message = "country cannot be blank.")
    private String country;
    @NotBlank(message = "location cannot be blank.")
    private Location location;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;

    @AllArgsConstructor
    @NoArgsConstructor
    @Component
    @Data
    @Builder
    @Validated
    public static class TheirAgeRequest {
        @NotBlank(message = "maximum age cannot be blank.")
        @Min(18)
        private int minAge;

        @NotBlank(message = "minimum age cannot be blank.")
        @Max(100)
        private int maxAge;

        @NotBlank(message = "phone cannot be blank.")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
        @Size(max = 15)
        private String phoneNumber;
    }
}
