package dev.lyneapp.backend.onboarding.model.archive;

import dev.lyneapp.backend.onboarding.model.Prompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class YourPromptsRequest {
    @Size(max = 3, message = "The number of prompts should be between 1 and 3.")
    private List<@NotBlank(message = "Answer cannot be blank.") @Size(max = 128, message = "Answer cannot be more than 128 characters.") Prompt> prompts;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}
