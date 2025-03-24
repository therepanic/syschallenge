/*
 * Copyright by the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syschallenge.shared.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.syschallenge.shared.service.StorageService;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * Service implementation for handling file storage operations using AWS S3
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AwsS3StorageService implements StorageService {

    private final S3Client client;

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        try {
            ResponseBytes<GetObjectResponse> objectBytes =
                    client.getObjectAsBytes(
                            GetObjectRequest.builder().bucket(bucketName).key(fileName).build());
            return objectBytes.asByteArray();
        } catch (S3Exception e) {
            throw new RuntimeException("File download error from S3", e);
        }
    }

    @Override
    public String uploadFile(String bucketName, byte[] file, String extension) {
        String fileName = UUID.randomUUID() + "-" + System.currentTimeMillis() + extension;
        client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(fileName).build(),
                RequestBody.fromBytes(file));
        return fileName;
    }

    @Override
    public void deleteFile(String bucketName, String fileName) {
        try {
            client.deleteObject(
                    DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build());
        } catch (S3Exception e) {
            throw new RuntimeException("File deletion error from S3", e);
        }
    }
}
