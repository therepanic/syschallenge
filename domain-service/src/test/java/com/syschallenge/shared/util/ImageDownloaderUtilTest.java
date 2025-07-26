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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class ImageDownloaderUtilTest {

	@Mock
	RestTemplate rest;

	@Test
	void download_returnsJpgFile_whenContentTypeIsJpeg() throws IOException {
		// arrange
		ImageDownloaderUtil util = new ImageDownloaderUtil(rest);

		byte[] fakeData = new byte[] { 1, 2, 3 };
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		ResponseEntity<byte[]> response = new ResponseEntity<>(fakeData, headers, 200);

		when(rest.getForEntity("http://test.com/image.jpg", byte[].class)).thenReturn(response);

		// act
		MultipartFile result = util.download("http://test.com/image.jpg");

		// assert
		assertEquals("downloaded.jpg", result.getOriginalFilename());
		assertEquals("image/jpeg", result.getContentType());
		assertArrayEquals(fakeData, result.getBytes());
	}

	@Test
	void download_returnsFileWithUnknownExtension_whenContentTypeIsUnknown() throws IOException {
		// arrange
		ImageDownloaderUtil util = new ImageDownloaderUtil(rest);

		byte[] fakeData = new byte[] { 4, 5, 6 };
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		ResponseEntity<byte[]> response = new ResponseEntity<>(fakeData, headers, 200);

		when(rest.getForEntity("http://test.com/file.pdf", byte[].class)).thenReturn(response);

		// act
		MultipartFile result = util.download("http://test.com/file.pdf");

		// assert
		assertEquals("downloaded", result.getOriginalFilename());
		assertEquals("application/pdf", result.getContentType());
		assertArrayEquals(fakeData, result.getBytes());
	}

}
