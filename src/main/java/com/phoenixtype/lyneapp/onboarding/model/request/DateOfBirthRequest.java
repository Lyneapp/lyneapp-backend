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
import java.time.LocalDate;


/**DOB stands for Date Of Birth */
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class DateOfBirthRequest {
    @NotBlank(message = "date of birth cannot be blank.")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}

