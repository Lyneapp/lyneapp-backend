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


@Component
@Validated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YourPhoneNumberRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters.")
    private String phoneNumber;
}