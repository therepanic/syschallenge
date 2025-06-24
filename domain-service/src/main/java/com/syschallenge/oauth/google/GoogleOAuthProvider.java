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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syschallenge.oauth.OAuthProvider;
import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.oauth.google.payload.request.GoogleOAuthV4TokenRequest;
import com.syschallenge.oauth.google.payload.response.GoogleOAuthV4TokenResponse;

import lombok.RequiredArgsConstructor;

/**
 * Google OAuth provider implementation
 *
 * @author therepanic
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class GoogleOAuthProvider implements OAuthProvider {

    private final GoogleOAuthV4Api googleOAuthApi;
    private final ObjectMapper mapper;
    private final GoogleOAuthProperty properties;

    @Override
    public OAuthUserInfo extractUser(String code) {
        GoogleOAuthV4TokenResponse response =
                this.googleOAuthApi.requestToken(
                        new GoogleOAuthV4TokenRequest(
                                this.properties.clientId(),
                                this.properties.clientSecret(),
                                this.properties.redirectUri(),
                                code,
                                this.properties.grantType()));
        return parseIdToken(response.idToken());
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfo parseIdToken(String idToken) {
        // highlight the center of the token
        String idTokenBody = idToken.replaceFirst("^[^.]+\\.([^.]+)\\.[^.]+$", "$1");
        String decodedIdTokenBody =
                new String(Base64.getDecoder().decode(idTokenBody), StandardCharsets.UTF_8);
        Map<String, Object> decodedIdTokenBodyMap;
        try {
            decodedIdTokenBodyMap = this.mapper.readValue(decodedIdTokenBody, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String email = (String) decodedIdTokenBodyMap.get("email");
        return new OAuthUserInfo(
                (String) decodedIdTokenBodyMap.get("sub"),
                email.split("@")[0],
                email,
                (String) decodedIdTokenBodyMap.get("picture"));
    }
}
