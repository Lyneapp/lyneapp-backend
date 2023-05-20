package dev.lyneapp.backendapplication.onboarding.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String userPhoneNumber;
    private String fromEmail;
    private String toEmail;
    private String emailAddress;
    private String subject;
    private String body;
}

