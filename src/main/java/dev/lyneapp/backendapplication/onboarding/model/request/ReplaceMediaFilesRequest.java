package dev.lyneapp.backendapplication.onboarding.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class ReplaceMediaFilesRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String userPhoneNumber;
    private List<String> mediaFileURLs;
}
