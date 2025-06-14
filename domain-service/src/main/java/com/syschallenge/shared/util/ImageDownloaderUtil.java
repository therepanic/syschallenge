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

package com.syschallenge.shared.util;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageDownloaderUtil {

    private final RestTemplate rest;

    public MultipartFile download(String url) {
        ResponseEntity<byte[]> response = rest.getForEntity(url, byte[].class);
        MediaType contentType = response.getHeaders().getContentType();
        String mimeType = contentType != null ? contentType.toString() : "application/octet-stream";
        String extension =
                switch (mimeType) {
                    case "image/jpeg", "image/jpg" -> ".jpg";
                    case "image/png" -> ".png";
                    default -> "";
                };
        String filename = "downloaded" + extension;
        return new SimpleMultipartFile("file", filename, mimeType, response.getBody());
    }
}
