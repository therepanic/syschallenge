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

package com.syschallenge.mainservice.oauth.github;

import com.syschallenge.mainservice.oauth.github.request.GithubOAuthTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class for interacting with the Github OAuth API
 *
 * @author panic08
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class GithubOAuthApi {

    private static final String GITHUB_OAUTH_API_URI = "https://github.com/login/oauth";

    private final RestTemplate restTemplate;

    /**
     * Requests an OAuth token from Github using the provided request data
     *
     * @param request {@link GithubOAuthTokenRequest} containing request details
     * @return containing access token
     */
    public String requestToken(GithubOAuthTokenRequest request) {
        return restTemplate.postForObject(GITHUB_OAUTH_API_URI + "/access_token", request, String.class);
    }
}
