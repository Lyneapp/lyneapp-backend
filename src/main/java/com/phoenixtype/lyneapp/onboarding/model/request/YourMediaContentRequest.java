package com.phoenixtype.lyneapp.onboarding.model.request;

import com.phoenixtype.lyneapp.onboarding.model.MediaFile;
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
public class YourMediaContentRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    private List<MediaFile> mediaFileList;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;
}
