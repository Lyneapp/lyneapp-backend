package dev.lyneapp.backend.onboarding.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.DeleteObjectsResult.DeletedObject;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket}")
    private String awsS3Bucket;

    public S3Service(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(String key, MultipartFile file) throws IOException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3Bucket, key, file.getInputStream(), metadata);
            s3Client.putObject(putObjectRequest);

        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload file to S3", e);
        }
    }

    public void deleteFiles(List<String> keys) {
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(awsS3Bucket)
                    .withKeys(keys.toArray(new String[keys.size()]));
            DeleteObjectsResult result = s3Client.deleteObjects(deleteObjectsRequest);

            List<DeletedObject> deletedObjects = result.getDeletedObjects();
            List<String> deletedObjectKeys = deletedObjects.stream().map(DeletedObject::getKey).collect(Collectors.toList());

            if (!deletedObjectKeys.containsAll(keys)) {
                throw new IllegalStateException("Failed to delete one or more objects from S3");
            }

        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to delete files from S3", e);
        } catch (SdkClientException e) {
            throw new IllegalStateException("Failed to communicate with S3", e);
        }
    }

    public void deleteFile(String key) {
        deleteFiles(Arrays.asList(key));
    }
}
