package dev.lyneapp.backendapplication.onboarding.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @NotBlank(message = "Latitude cannot be blank")
    private double latitude;
    @NotBlank(message = "Longitude cannot be blank")
    private double longitude;
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotBlank(message = "Country cannot be blank")
    private String country;
}
