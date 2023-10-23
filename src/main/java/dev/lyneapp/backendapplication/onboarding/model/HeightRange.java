package dev.lyneapp.backendapplication.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeightRange {
    private String userPhoneNumber;
    private int minimumHeight;
    private int maximumHeight;
    private boolean openToAll;
}
