package dev.lyneapp.backendapplication.onboarding.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class ConfirmEmailRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String userPhoneNumber;
    @NotBlank(message = "Confirmation token must be 6 characters long.")
    @Pattern(regexp = "^[A-Z]{4}\\d{4}$")
    @Size(min = 8, max = 8)
    private String confirmationToken;
}