package com.phoenixtype.lyneapp.preferences.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**WDYD stands for what do you do */
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class TheirOccupationRequest {
    @NotBlank(message = "occupation cannot be blank.")
    private String occupation;
    @NotBlank(message = "industry cannot be blank.")
    private String industry;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}