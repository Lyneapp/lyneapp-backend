package dev.lyneapp.backendapplication.onboarding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Qualification {
    private String userPhoneNumber;
    private String highestDegree;
    private String institutionName;
}
