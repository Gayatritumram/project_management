package com.backend.project_management.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;

@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    }

    public String uploadImage(MultipartFile file) throws IOException {

        java.nio.file.Path tempPath = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempPath.toFile());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempPath));

    }
}
