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

package com.syschallenge.mainservice.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syschallenge.mainservice.oauth.model.GoogleOAuthIdTokenUser;
import com.syschallenge.mainservice.oauth.request.GoogleOAuthV4TokenRequest;
import com.syschallenge.mainservice.oauth.response.GoogleOAuthV4TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * A service class for interacting with the Google OAuth V4
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class GoogleOAuthV4Service {

    private final GoogleOAuthV4Api googleOAuthApi;
    private final ObjectMapper mapper;

    /**
     * Extracts user information from a Google OAuth ID token
     * <p>
     * Requests a Google OAuth token and decodes the ID token to retrieve user info
     *
     * @param clientId Google application client ID
     * @param clientSecret client secret key
     * @param redirectUri redirect URI after authentication
     * @param code authorization code from Google
     * @param grantType type of grant requested
     * @return {@link GoogleOAuthIdTokenUser} object with user information extracted from the ID token
     * @throws RuntimeException if an error occurs during ID token decoding or parsing
     */
    public GoogleOAuthIdTokenUser extractGoogleOAuthIdTokenUser(String clientId,
                                                                String clientSecret,
                                                                String redirectUri,
                                                                String code,
                                                                String grantType) {
        GoogleOAuthV4TokenResponse googleOAuthV4TokenResponse =
                googleOAuthApi.requestToken(new GoogleOAuthV4TokenRequest(clientId, clientSecret, redirectUri, code, grantType));

        String idToken = googleOAuthV4TokenResponse.idToken();
        // highlight the center of the token
        String idTokenBody = idToken.replaceFirst("^[^.]+\\.([^.]+)\\.[^.]+$", "$1");
        String decodedIdTokenBody = new String(Base64.getDecoder().decode(idTokenBody), StandardCharsets.UTF_8);
        Map<String, Object> decodedIdTokenBodyMap;
        try {
            decodedIdTokenBodyMap = mapper.readValue(decodedIdTokenBody, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new GoogleOAuthIdTokenUser(
                (String) decodedIdTokenBodyMap.get("sub"),
                (String) decodedIdTokenBodyMap.get("email"),
                (boolean) decodedIdTokenBodyMap.get("email_verified"),
                (String) decodedIdTokenBodyMap.get("name"),
                (String) decodedIdTokenBodyMap.get("picture"),
                (String) decodedIdTokenBodyMap.get("given_name")
        );
    }
}
