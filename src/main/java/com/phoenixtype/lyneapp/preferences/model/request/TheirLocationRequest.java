package com.phoenixtype.lyneapp.preferences.model.request;

import com.phoenixtype.lyneapp.onboarding.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class TheirLocationRequest {

    @NotBlank(message = "city cannot be blank.")
    private String city;
    @NotBlank(message = "country cannot be blank.")
    private String country;
    @NotBlank(message = "location cannot be blank.")
    private Location location;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}
