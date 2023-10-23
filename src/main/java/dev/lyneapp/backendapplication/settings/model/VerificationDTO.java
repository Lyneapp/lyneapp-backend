package dev.lyneapp.backendapplication.settings.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class VerificationDTO {
    private String userPhoneNumber;
    private String selfieImage;
    private byte[] selfieImageData;
    private String idFrontImage;
    private byte[] idFrontImageData;
    private String idBackImage;
    private byte[] idBackImageData;
    private String selfieImageUrl;
    private String idFrontImageUrl;
    private String idBackImageUrl;
    // FIXME: Store images in S3 and save to DB
}
