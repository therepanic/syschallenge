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

/**
 * Service interface for handling file storage operations
 *
 * @author therepanic
 * @since 1.0.0
 */
public interface FileStorageService {

    /**
     * Downloads a file from the specified bucket
     *
     * @param bucketName the name of the bucket containing the file
     * @param fileName the name of the file to download
     * @return a byte array containing the downloaded file content
     */
    byte[] downloadFile(String bucketName, String fileName);

    /**
     * Uploads a file to the specified bucket
     *
     * @param bucketName the name of the bucket where the file will be uploaded
     * @param file the file to upload
     * @param extension the file extension
     * @return the file key or URL of the uploaded file
     */
    String uploadFile(String bucketName, byte[] file, String extension);

    /**
     * Deletes a file from the specified bucket
     *
     * @param bucketName the name of the bucket containing the file
     * @param fileName the name of the file to delete
     */
    void deleteFile(String bucketName, String fileName);
}
