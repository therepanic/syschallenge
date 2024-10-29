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

import com.syschallenge.mainservice.oauth.request.GoogleOAuthV4TokenRequest;
import com.syschallenge.mainservice.oauth.response.GoogleOAuthV4TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class for interacting with the Google OAuth V4 API
 *
 * @author panic08
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class GoogleOAuthV4Api {
    private static final String GOOGLE_OAUTH_V4_API_URI = "https://www.googleapis.com/oauth2/v4";

    private final RestTemplate restTemplate;

    /**
     * Requests an OAuth token from Google using the provided request data
     *
     * @param request {@link GoogleOAuthV4TokenRequest} containing request details
     * @return {@link GoogleOAuthV4TokenResponse} containing response details
     */
    public GoogleOAuthV4TokenResponse requestToken(GoogleOAuthV4TokenRequest request) {
        return restTemplate.postForObject(GOOGLE_OAUTH_V4_API_URI + "/token", request, GoogleOAuthV4TokenResponse.class);
    }
}
