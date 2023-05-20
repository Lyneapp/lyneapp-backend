package dev.lyneapp.backendapplication.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Religion {
    private String preferredReligion;
    private boolean openToAll;
    private String Denomination;
}
