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

package com.syschallenge.oauth.google;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.oauth.google.payload.response.GoogleOAuthV4TokenResponse;

/**
 * @author panic08
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class GoogleOAuthProviderTest {
    @Mock private GoogleOAuthV4Api googleOAuthApi;
    @Mock private ObjectMapper mapper;
    @Mock private GoogleOAuthProperty properties;

    @InjectMocks private GoogleOAuthProvider googleOAuthProvider;

    private final String testCode = "test_code";
    private final String testIdToken =
            "header.eyJzdWIiOiJnb29nbGVfdXNlcl8xMjMiLCJlbWFpbCI6InRlc3RAZXhhbXBsZS5jb20ifQ==.signature";

    @Test
    void extractUser_ValidCode_ReturnsUserInfo() throws Exception {
        // Arrange
        GoogleOAuthV4TokenResponse tokenResponse =
                new GoogleOAuthV4TokenResponse(
                        "access_token_123", 3600, "openid profile email", "Bearer", testIdToken);

        Map<String, Object> testClaims =
                Map.of(
                        "sub",
                        "google_user_123",
                        "email",
                        "test@example.com",
                        "email_verified",
                        true,
                        "name",
                        "Test User",
                        "picture",
                        "http://picture.url",
                        "given_name",
                        "Test");

        when(properties.clientId()).thenReturn("client_id");
        when(googleOAuthApi.requestToken(any())).thenReturn(tokenResponse);
        when(mapper.readValue(anyString(), eq(Map.class))).thenReturn(testClaims);

        // Act
        OAuthUserInfo userInfo = googleOAuthProvider.extractUser(testCode);

        // Assert
        assertThat(userInfo)
                .hasFieldOrPropertyWithValue("providerUserId", "google_user_123")
                .hasFieldOrPropertyWithValue("email", "test@example.com");
    }
}
