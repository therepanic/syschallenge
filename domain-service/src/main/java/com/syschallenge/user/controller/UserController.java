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

package com.syschallenge.user.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.syschallenge.user.payload.response.PhotoResponse;
import com.syschallenge.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling user-related operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint for retrieving photo for a specific user
     *
     * @param id the UUID of the user to fetch photo for
     * @return photo
     */
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable("id") UUID id) {
        PhotoResponse photo = this.userService.getPhoto(id);
        if (photo.photo() == null) return ResponseEntity.notFound().build();
        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(photo.photoFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MediaType mediaType =
                contentType != null ? MediaType.parseMediaType(contentType) : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(mediaType).body(photo.photo());
    }

    /**
     * Endpoint for uploading photo for a specific user
     *
     * @param id the UUID of the user to fetch photo for
     * @param photoFile photo to upload
     * @param auth the authentication token containing the requester's identity
     * @return photo
     */
    @PostMapping("/{id}/photo")
    public ResponseEntity<byte[]> uploadPhoto(
            @PathVariable("id") UUID id,
            @RequestPart("file") MultipartFile photoFile,
            UsernamePasswordAuthenticationToken auth) {
        PhotoResponse photo =
                this.userService.uploadPhoto(id, photoFile, UUID.fromString(auth.getName()));
        if (photo.photo() == null) return ResponseEntity.notFound().build();
        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(photo.photoFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MediaType mediaType =
                contentType != null ? MediaType.parseMediaType(contentType) : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(mediaType).body(photo.photo());
    }
}
