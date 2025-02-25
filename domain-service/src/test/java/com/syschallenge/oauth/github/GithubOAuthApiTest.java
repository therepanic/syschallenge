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

package com.syschallenge.oauth.github;

import com.syschallenge.oauth.github.payload.request.GithubOAuthTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author panic08
 * @since 1.0.0
 */
class GithubOAuthApiTest {

    private GithubOAuthApi githubOAuthApi;

    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        this.githubOAuthApi = new GithubOAuthApi(restTemplate);
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testRequestToken() {
        // Given
        GithubOAuthTokenRequest request = new GithubOAuthTokenRequest(
                "test-client-id",
                "test-client-secret",
                "test-code",
                "http://test-redirect-uri"
        );

        String expectedResponseBody = "access_token=gho_testToken&scope=repo&token_type=bearer";

        mockRestServiceServer.expect(requestTo("https://github.com/login/oauth/access_token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                {
                    "client_id": "test-client-id",
                    "client_secret": "test-client-secret",
                    "code": "test-code",
                    "redirect_uri": "http://test-redirect-uri"
                }
                """))
                .andRespond(withSuccess(expectedResponseBody, MediaType.APPLICATION_FORM_URLENCODED));

        // When
        String response = githubOAuthApi.requestToken(request);

        // Then
        assertEquals(expectedResponseBody, response);
        mockRestServiceServer.verify();
    }
}
