package dev.lyneapp.backendapplication.onboarding.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRegistrationResponse {
    private String token;
    private String userPhoneNumber;
    private boolean accountEnabled;
    private LocalDateTime passwordCreatedDate;
}
