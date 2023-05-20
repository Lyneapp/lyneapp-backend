package dev.lyneapp.backendapplication.onboarding.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String userPhoneNumber;
    private boolean profileCreated;
}
