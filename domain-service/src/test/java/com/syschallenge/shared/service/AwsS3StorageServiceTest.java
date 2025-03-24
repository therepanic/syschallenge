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

package com.syschallenge.shared.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.shared.service.impl.AwsS3StorageService;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * @author panic08
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class AwsS3StorageServiceTest {
    @Mock private S3Client s3Client;

    @InjectMocks private AwsS3StorageService awsS3StorageService;

    private static final String BUCKET_NAME = "test-bucket";
    private static final String FILE_NAME = "test-file.txt";

    @Test
    void testDownloadFile_success() {
        // Arrange
        byte[] expectedBytes = "test content".getBytes();
        ResponseBytes<GetObjectResponse> responseBytes = mock(ResponseBytes.class);
        when(responseBytes.asByteArray()).thenReturn(expectedBytes);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(responseBytes);

        // Act
        byte[] result = awsS3StorageService.downloadFile(BUCKET_NAME, FILE_NAME);

        // Assert
        assertArrayEquals(expectedBytes, result);
        verify(s3Client, times(1)).getObjectAsBytes(any(GetObjectRequest.class));
    }

    @Test
    void testDownloadFile_failure() {
        // Arrange
        S3Exception s3Exception = (S3Exception) S3Exception.builder().message("S3 error").build();
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenThrow(s3Exception);

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> awsS3StorageService.downloadFile(BUCKET_NAME, FILE_NAME));
        assertEquals("File download error from S3", exception.getMessage());
        verify(s3Client, times(1)).getObjectAsBytes(any(GetObjectRequest.class));
    }

    @Test
    void testUploadFile_success() {
        // Arrange
        byte[] fileContent = "test content".getBytes();
        String extension = ".txt";
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        // Act
        String resultFileName = awsS3StorageService.uploadFile(BUCKET_NAME, fileContent, extension);

        // Assert
        assertNotNull(resultFileName);
        assertTrue(resultFileName.endsWith(extension));
        assertTrue(resultFileName.contains("-"));
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testDeleteFile_success() {
        // Arrange
        when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
                .thenReturn(DeleteObjectResponse.builder().build());

        // Act
        awsS3StorageService.deleteFile(BUCKET_NAME, FILE_NAME);

        // Assert
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void testDeleteFile_failure() {
        // Arrange
        S3Exception s3Exception = (S3Exception) S3Exception.builder().message("S3 error").build();
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenThrow(s3Exception);

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> awsS3StorageService.deleteFile(BUCKET_NAME, FILE_NAME));
        assertEquals("File deletion error from S3", exception.getMessage());
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
