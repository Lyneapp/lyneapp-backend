package dev.lyneapp.backend.onboarding.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class YourPasswordRequest {
    @NotBlank(message = "Password cannot be blank.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Password must have at least 8 and at most of 20 characters and contain at least one uppercase letter, " +
                    "one lowercase letter, one number, and one special character.")
    @Size(min = 8, max = 20, message = "Password cannot be longer than 20 nor shorter than 8 characters.")
//    @Getter(AccessLevel.NONE)
    private String password;

    @NotBlank(message = "Confirm password cannot be blank.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$",
            message = "Confirm password must have at least 8 and at most of 20 characters and contain at least one uppercase letter, " +
                    "one lowercase letter, one number, and one special character.")
    @Size(min = 8, max = 20, message = "Confirm password cannot be longer than 20 nor shorter than 8 characters.")
//    @Getter(AccessLevel.NONE)
    private String confirmPassword;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;

    private boolean accountEnabled;

}
