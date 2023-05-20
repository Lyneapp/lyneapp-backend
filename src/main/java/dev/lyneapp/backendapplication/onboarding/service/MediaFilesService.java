package dev.lyneapp.backendapplication.onboarding.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mongodb.client.result.UpdateResult;
import dev.lyneapp.backendapplication.onboarding.model.User;
import dev.lyneapp.backendapplication.onboarding.model.request.DeleteMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.GetMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.UploadMediaFilesRequest;
import dev.lyneapp.backendapplication.onboarding.repository.UserRepository;
import dev.lyneapp.backendapplication.onboarding.util.exception.UploadFailedException;
import dev.lyneapp.backendapplication.onboarding.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static dev.lyneapp.backendapplication.onboarding.util.VerifyUser.verifyPhoneNumberExist;
import static dev.lyneapp.backendapplication.onboarding.util.exception.ExceptionMessages.FAILED_TO_UPLOAD_IMAGE_S3;
import static dev.lyneapp.backendapplication.onboarding.util.exception.ExceptionMessages.USER_NOT_FOUND_WITH_ID;


@Service
@RequiredArgsConstructor
public class MediaFilesService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MediaFilesService.class);

    @Value("${aws_s3_media_bucket}")
    private String awsS3MediaBucket;
    @Value("${amazonaws.com}")
    private String amazonawsDotCom;

    private final UserRepository userRepository;
    private final AmazonS3 s3Client;
    private final MongoTemplate mongoTemplate;


    public void uploadMediaFiles(MultipartFile[] mediaFiles, UploadMediaFilesRequest uploadMediaFilesRequest) {
        Optional<User> user = userRepository.findByUserPhoneNumber(uploadMediaFilesRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        String id = verifiedUser.getId();

        for (MultipartFile mediaFile : mediaFiles) {
            String imageUrl = uploadMediaFileTos3(id, mediaFile);
            saveMediaFilesURLToMongo(id, imageUrl);
        }
    }


    public List<String> getAllMediaFileUrls(GetMediaFilesRequest getMediaFilesRequest) {
        Optional<User> user = userRepository.findByUserPhoneNumber(getMediaFilesRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        String id = verifiedUser.getId();

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        return verifiedUser.getMediaFileURLs();
    }


    public List<String> deleteMediaFilesUrl(DeleteMediaFilesRequest deleteMediaFilesRequest) {
        List<String> remainingMediaFileUrls = new ArrayList<>();
        Optional<User> user = userRepository.findByUserPhoneNumber(deleteMediaFilesRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        String id = verifiedUser.getId();

        for (String mediaFileUrl : deleteMediaFilesRequest.getMediaFileURLs()) {
            String key = extractS3KeyFromUrl(mediaFileUrl);
            s3Client.deleteObject(awsS3MediaBucket, key);

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(id));
            Update update = new Update();
            update.pull("mediaFileURLs", mediaFileUrl);
            UpdateResult updatedResult = mongoTemplate.updateFirst(query, update, User.class);

            if (updatedResult.getMatchedCount() == 0) {
                throw new UserNotFoundException(USER_NOT_FOUND_WITH_ID + id);
            } else {
                remainingMediaFileUrls.add(mediaFileUrl);
            }
        }
        return remainingMediaFileUrls;
    }


    private String uploadMediaFileTos3(String id, MultipartFile mediaFile) {
        String mediaFileName = UUID.randomUUID().toString();
        String mediaFileURL = "https://" + awsS3MediaBucket + ".s3." + s3Client.getRegion() + "." + amazonawsDotCom + "/" + id + "/" + mediaFileName;

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(mediaFile.getContentType());
            objectMetadata.setContentLength(mediaFile.getSize());
            s3Client.putObject(awsS3MediaBucket, id + "/" + mediaFileName, mediaFile.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new UploadFailedException((String) FAILED_TO_UPLOAD_IMAGE_S3, e);
        }
        return mediaFileURL;
    }


    private void saveMediaFilesURLToMongo(String id, String mediaFileUrl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.addToSet("mediaFileURLs", mediaFileUrl);
        UpdateResult updatedResult = mongoTemplate.updateFirst(query, update, User.class);

        if (updatedResult.getMatchedCount() == 0) {
            throw new UserNotFoundException(USER_NOT_FOUND_WITH_ID + id);
        }
    }


    private String extractS3KeyFromUrl(String mediaFileUrl) {
        String[] parts = mediaFileUrl.split("/");
        int length = parts.length;

        StringBuilder s3KeyBuilder = new StringBuilder();
        for (int i = 3; i < length; i++) {
            s3KeyBuilder.append(parts[i]);
            if (i < length - 1) {
                s3KeyBuilder.append("/");
            }
        }
        return s3KeyBuilder.toString();
    }
}
