package com.example.demo.service;

import io.minio.*;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {
    private MinioClient minioClient;

    @Autowired
    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void createBucket(String bucketName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        boolean isExist = minioClient.bucketExists(
            BucketExistsArgs.builder().bucket(bucketName).build()
        );
        if (!isExist) {
            minioClient.makeBucket(
                MakeBucketArgs.builder().bucket(bucketName).build()
            );
            System.out.println("Bucket created successfully.");
        } else {
            System.out.println("Bucket already exists.");
        }
    }

    public void uploadFile(String bucketName, String objectName, InputStream fileStream, String contentType) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(fileStream, fileStream.available(), -1)
                .contentType(contentType)
                .build()
        );
    }

    public InputStream downloadFile(String bucketName, String objectName) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build()
        );
    }
}
