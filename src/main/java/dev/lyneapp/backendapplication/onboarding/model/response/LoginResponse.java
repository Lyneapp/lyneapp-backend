package dev.lyneapp.backendapplication.onboarding.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private boolean userLoggedIn;
    private String userPhoneNumber;
}

