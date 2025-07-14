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

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.syschallenge.oauth.github.payload.request.GitHubOAuthTokenRequest;

import lombok.RequiredArgsConstructor;

/**
 * A component class for interacting with the GitHub OAuth API
 *
 * @author therepanic
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class GitHubOAuthApi {

    private static final String GITHUB_OAUTH_API_URI = "https://github.com/login/oauth";

    private final RestClient restClient;

    /**
     * Requests an OAuth token from GitHub using the provided request data
     *
     * @param request {@link GitHubOAuthTokenRequest} containing request details
     * @return containing access token
     */
    public String requestToken(GitHubOAuthTokenRequest request) {
        return this.restClient
                .post()
                .uri(GITHUB_OAUTH_API_URI + "/access_token")
                .body(request)
                .retrieve()
                .toEntity(String.class)
                .getBody();
    }
}
