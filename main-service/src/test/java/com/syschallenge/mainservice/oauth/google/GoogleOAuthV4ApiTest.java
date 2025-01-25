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

package com.syschallenge.mainservice.oauth.google;

import com.syschallenge.mainservice.oauth.google.request.GoogleOAuthV4TokenRequest;
import com.syschallenge.mainservice.oauth.google.response.GoogleOAuthV4TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author panic08
 * @since 1.0.0
 */
class GoogleOAuthV4ApiTest {

    private GoogleOAuthV4Api googleOAuthApi;
    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();

        this.googleOAuthApi = new GoogleOAuthV4Api(restTemplate);
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testRequestToken() {
        GoogleOAuthV4TokenRequest request = new GoogleOAuthV4TokenRequest(null, null, null, null, null);

        String expectedResponse = "{ " +
                "\"access_token\": \"test-access_token\", " +
                "\"expires_in\": 1, " +
                "\"scope\": \"test-scope\", " +
                "\"token_type\": \"test-token_type\", " +
                "\"id_token\": \"test-id_token\" " +
                "}";

        mockRestServiceServer.expect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.requestTo("https://www.googleapis.com/oauth2/v4/token"))
                .andRespond(MockRestResponseCreators.withSuccess(expectedResponse, MediaType.APPLICATION_JSON));

        GoogleOAuthV4TokenResponse response = googleOAuthApi.requestToken(request);

        assertEquals("test-access_token", response.accessToken());
        assertEquals(1, response.expiresIn());
        assertEquals("test-scope", response.scope());
        assertEquals("test-token_type", response.tokenType());
        assertEquals("test-id_token", response.idToken());

        mockRestServiceServer.verify();
    }
}
