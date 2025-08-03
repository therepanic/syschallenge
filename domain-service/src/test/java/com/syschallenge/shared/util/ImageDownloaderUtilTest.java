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
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.IOException;

import com.syschallenge.oauth.github.GitHubOAuthApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author therepanic
 * @since 1.0.0
 */
@RestClientTest
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImageDownloaderUtilTest {

	@Autowired
	private RestClient.Builder restClient;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	private ImageDownloaderUtil util;

	@BeforeEach
	void setUp() {
		this.util = new ImageDownloaderUtil(restClient.build());
	}

	@Test
	void download_returnsJpgFile_whenContentTypeIsJpeg() throws IOException {
		// arrange
		byte[] fakeData = new byte[] { 1, 2, 3 };
		mockRestServiceServer.expect(requestTo("http://test.com/image.jpg"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(fakeData, MediaType.IMAGE_JPEG));

		// act
		MultipartFile result = util.download("http://test.com/image.jpg");

		// assert
		assertEquals("downloaded.jpg", result.getOriginalFilename());
		assertEquals("image/jpeg", result.getContentType());
		assertArrayEquals(fakeData, result.getBytes());
		mockRestServiceServer.verify();
	}

	@Test
	void download_returnsFileWithUnknownExtension_whenContentTypeIsUnknown() throws IOException {
		// arrange
		byte[] fakeData = new byte[] {4, 5, 6};
		mockRestServiceServer.expect(requestTo("http://test.com/file.pdf"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withSuccess(fakeData, MediaType.APPLICATION_PDF));

		// act
		MultipartFile result = util.download("http://test.com/file.pdf");

		// assert
		assertEquals("downloaded", result.getOriginalFilename());
		assertEquals("application/pdf", result.getContentType());
		assertArrayEquals(fakeData, result.getBytes());
		mockRestServiceServer.verify();
	}

}
