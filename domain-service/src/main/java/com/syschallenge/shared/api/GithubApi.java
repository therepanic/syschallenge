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

package com.syschallenge.shared.api;

import com.syschallenge.shared.api.response.GithubUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class for interacting with the Github API
 *
 * @author panic08
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class GithubApi {

    private static final String GITHUB_API_URI = "https://api.github.com";

    private final RestTemplate restTemplate;

    public GithubUser getUser(String accessToken) {
        return restTemplate.exchange(
                GITHUB_API_URI + "/user",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders() {{ setBearerAuth(accessToken); }}),
                GithubUser.class
        ).getBody();
    }
}
