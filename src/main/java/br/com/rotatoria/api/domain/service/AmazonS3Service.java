package br.com.rotatoria.api.domain.service;

import br.com.rotatoria.api.domain.dto.video.PresignedPutUrlDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AmazonS3Service {

    private final S3Client s3client;
    private final S3Presigner s3Presigner;

    public AmazonS3Service(S3Client s3client, S3Presigner s3Presigner) {
        this.s3client = s3client;
        this.s3Presigner = s3Presigner;
    }

    public String createPresignedGetUrl(String fileName, String bucketName) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .responseContentDisposition("inline")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(1))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }

    public PresignedPutUrlDTO createPresignedPutUrl(String originalFileName, String bucketName, String extension) {
        String formattedFileName = generateFileName(originalFileName, extension);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(formattedFileName)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        var dto = new PresignedPutUrlDTO();
        dto.setFileName(formattedFileName);
        dto.setUrl(presignedRequest.url().toExternalForm());

        return dto;
    }

    private String generateFileName(String fileName, String extension) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = LocalDateTime.now().format(dateFormatter);
        return fileName + "-" + date + "-" + System.currentTimeMillis() + extension;
    }
}