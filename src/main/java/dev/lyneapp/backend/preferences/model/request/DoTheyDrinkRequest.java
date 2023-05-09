package dev.lyneapp.backend.preferences.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/** DYD is do you drink? */

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class DoTheyDrinkRequest {
    @NotBlank(message = "do you drink cannot be blank.")
    private boolean doYouDrink;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}

