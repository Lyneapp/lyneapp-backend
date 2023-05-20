package dev.lyneapp.backendapplication.onboarding.controller;


import dev.lyneapp.backendapplication.onboarding.model.request.DeleteMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.GetMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.UploadMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.service.MediaFilesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 To test the endpoint using Postman, you can follow these steps:

 Open Postman and create a new POST request for the endpoint that you have created in your Spring Boot application.

 In the request body, select the "form-data" option, and add a key-value pair for each of the six images that you want to upload. Set the key as "images[]" and the value as the corresponding image file. Repeat this step for all six images.

 Set the content type of the request to "multipart/form-data".

 Send the request to the endpoint and check the response.

 If the images are uploaded successfully, you should get a success response with the URLs of the uploaded images. If there are any errors, you will receive an error response indicating the reason for the failure.

 Note: You may need to adjust the URL and port number in Postman to match the endpoint URL and port number of your Spring Boot application.
 */

// TODO How does the front end get the id and media file
// TODO CRUD operation on a single media file
// TODO CRUD operation on the entire media files
// TODO Remember to handle appropriate validations and error cases for each of these operations, such as checking ownership, file size limits, and handling any potential errors during the process.

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class MediaFilesController {

    private final MediaFilesService mediaFilesService;


    @PostMapping(path = "uploadMedia", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadMediaFiles(@RequestParam("mediaFile") MultipartFile[] mediaFile, @RequestParam("userPhoneNumber") @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$") @Size(max = 15) String userPhoneNumber
    ) {
        UploadMediaFilesRequest uploadMediaFilesRequest = new UploadMediaFilesRequest();
        uploadMediaFilesRequest.setUserPhoneNumber(userPhoneNumber);
        mediaFilesService.uploadMediaFiles(mediaFile, uploadMediaFilesRequest);
        return ResponseEntity.ok().build();
    }


    @GetMapping(path = "getMedia", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getAllMediaFileUrls(@Valid @RequestBody GetMediaFilesRequest getMediaFilesRequest) {
        List<String> mediaFileURLs = mediaFilesService.getAllMediaFileUrls(getMediaFilesRequest);
        return ResponseEntity.ok(mediaFileURLs);
    }


    @PostMapping(path = "deleteMedia", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> deleteMediaFilesUrl(@Valid @RequestBody DeleteMediaFilesRequest DeleteMediaFilesRequest) {
        List<String> remainingUrls = mediaFilesService.deleteMediaFilesUrl(DeleteMediaFilesRequest);
        return ResponseEntity.ok(remainingUrls);
    }
}
