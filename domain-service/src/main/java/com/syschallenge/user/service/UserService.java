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

package com.syschallenge.user.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.exception.PermissionDeniedException;
import com.syschallenge.shared.service.StorageService;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserBasicInfo;
import com.syschallenge.user.model.UserPhoto;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.payload.response.PhotoResponse;
import com.syschallenge.user.repository.UserBasicInfoRepository;
import com.syschallenge.user.repository.UserPhotoRepository;
import com.syschallenge.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Service for handling user related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final UserBasicInfoRepository userBasicInfoRepository;
    private final StorageService storageService;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".png", ".jpg", ".jpeg");
    private static final String USERS_PHOTO_BUCKET = "users-photo";

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User create(OAuthUserInfo userInfo) {
        User newUser =
                User.builder()
                        .email(userInfo.email())
                        .username(userInfo.username())
                        .role(UserRole.DEFAULT)
                        .registeredAt(LocalDateTime.now())
                        .build();
        newUser = this.userRepository.save(newUser);
        this.userBasicInfoRepository.save(
                UserBasicInfo.builder()
                        .userId(newUser.getId())
                        .name(newUser.getUsername())
                        .build());
        return newUser;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String getUsernameById(UUID id) {
        return this.userRepository.findUsernameById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User getById(UUID id) {
        return this.userRepository.findById(id);
    }

    public PhotoResponse getPhoto(UUID id) {
        String objectKey = this.userPhotoRepository.findObjectKeyByUserId(id);
        if (objectKey == null) return null;
        return new PhotoResponse(
                this.storageService.downloadFile(USERS_PHOTO_BUCKET, objectKey), objectKey);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PhotoResponse uploadPhoto(UUID id, MultipartFile photoFile, UUID principalId)
            throws PermissionDeniedException {
        if (!isExtensionValid(photoFile)) {
            throw new IllegalArgumentException("Unsupported extension type");
        }
        if (!id.equals(principalId)
                && this.userRepository.findRoleById(principalId).equals(UserRole.DEFAULT)) {
            throw new PermissionDeniedException("You can only update your own occupation.");
        }
        byte[] resizedPhotoFile;
        try (InputStream inputStream = photoFile.getInputStream()) {
            BufferedImage resizedImage =
                    Thumbnails.of(inputStream).size(200, 200).asBufferedImage();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", outputStream);
            resizedPhotoFile = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String extension =
                photoFile
                        .getOriginalFilename()
                        .substring(photoFile.getOriginalFilename().lastIndexOf("."));
        if (this.userPhotoRepository.existsByUserId(id)) {
            String userPhotoObjectKey = this.userPhotoRepository.findObjectKeyByUserId(id);
            this.storageService.deleteFile(USERS_PHOTO_BUCKET, userPhotoObjectKey);
            String newUserPhotoObjectKey =
                    this.storageService.uploadFile(USERS_PHOTO_BUCKET, resizedPhotoFile, extension);
            this.userPhotoRepository.updateObjectKeyByUserId(newUserPhotoObjectKey, id);
            return new PhotoResponse(resizedPhotoFile, newUserPhotoObjectKey);
        } else {
            String newUserPhotoObjectKey =
                    this.storageService.uploadFile(USERS_PHOTO_BUCKET, resizedPhotoFile, extension);
            UserPhoto newUserPhoto =
                    UserPhoto.builder().userId(id).objectKey(newUserPhotoObjectKey).build();
            this.userPhotoRepository.save(newUserPhoto);
            return new PhotoResponse(resizedPhotoFile, newUserPhotoObjectKey);
        }
    }

    private boolean isExtensionValid(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (!filename.contains(".")) {
            return false;
        }
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}
