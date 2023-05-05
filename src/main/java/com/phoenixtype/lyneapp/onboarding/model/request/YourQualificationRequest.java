package com.phoenixtype.lyneapp.onboarding.model.request;

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
public class YourQualificationRequest {
    @NotBlank(message = "highest level of education cannot be blank.")
    private String highestLevelOfEducation;
    @NotBlank(message = "institution name cannot be blank.")
    private String institutionName;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}
