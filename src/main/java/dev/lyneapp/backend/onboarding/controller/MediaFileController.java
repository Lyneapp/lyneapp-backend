package dev.lyneapp.backend.onboarding.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import dev.lyneapp.backend.onboarding.model.MediaFile;
import dev.lyneapp.backend.onboarding.model.request.YourMediaContentRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/v1")
@AllArgsConstructor
public class MediaFileController {

    private AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String awsS3Bucket;

    @PostMapping(path = "upload_media")
    public ResponseEntity<List<String>> uploadFile(@ModelAttribute YourMediaContentRequest yourMediaContentRequest) throws IOException {
        List<String> fileUrls = yourMediaContentRequest.getMediaFiles().stream().map(mediaFile -> {
            String fileUrl = "";
            try {
                fileUrl = uploadToS3(mediaFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileUrl;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(fileUrls, HttpStatus.OK);
    }

    private String uploadToS3(MediaFile mediaFile) throws IOException {
        String fileName = mediaFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(mediaFile.getSize());
        metadata.setContentType(mediaFile.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3Bucket, fileName, mediaFile.getInputStream(), metadata);
        putObjectRequest.withCannedAcl(com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(awsS3Bucket, fileName).toString();
    }
}
