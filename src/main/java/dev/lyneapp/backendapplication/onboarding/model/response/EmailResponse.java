package dev.lyneapp.backendapplication.onboarding.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailResponse {
    private String emailAddress;
    private String userPhoneNumber;
    private String confirmationStatus;
    private boolean emailEnabled;
    private LocalDateTime confirmedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private String token;
}