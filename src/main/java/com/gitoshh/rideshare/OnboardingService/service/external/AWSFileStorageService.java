package com.gitoshh.rideshare.OnboardingService.service.external;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gitoshh.rideshare.OnboardingService.contracts.FileStorageInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class AWSFileStorageService implements FileStorageInterface {

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3_bucket}")
    private String bucketName;

    public String uploadFile(String fileName,
                             InputStream inputStream,
                             Map<String, String> optionalMetaData
    ) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.forEach(objectMetadata::addUserMetadata);
        try {
            PutObjectResult result = amazonS3Client.putObject(bucketName, fileName, inputStream, objectMetadata);
            return result.getETag();
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }
}
