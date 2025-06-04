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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.syschallenge.shared.api.payload.response.GithubUser;

/**
 * @author therepanic
 * @since 1.0.0
 */
class GithubApiTest {

    private GithubApi githubApi;
    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        this.githubApi = new GithubApi(restTemplate);
        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetUser() {
        // Given
        String accessToken = "test-access-token";
        String expectedResponseBody =
                """
				    {
				    "login": "panic08",
				    "id": 120543954,
				    "node_id": "U_kgDOBy9a0g",
				    "avatar_url": "https://avatars.githubusercontent.com/u/120543954?v=4",
				    "gravatar_id": "",
				    "url": "https://api.github.com/users/panic08",
				    "html_url": "https://github.com/panic08",
				    "followers_url": "https://api.github.com/users/panic08/followers",
				    "following_url": "https://api.github.com/users/panic08/following{/other_user}",
				    "gists_url": "https://api.github.com/users/panic08/gists{/gist_id}",
				    "starred_url": "https://api.github.com/users/panic08/starred{/owner}{/repo}",
				    "subscriptions_url": "https://api.github.com/users/panic08/subscriptions",
				    "organizations_url": "https://api.github.com/users/panic08/orgs",
				    "repos_url": "https://api.github.com/users/panic08/repos",
				    "events_url": "https://api.github.com/users/panic08/events{/privacy}",
				    "received_events_url": "https://api.github.com/users/panic08/received_events",
				    "type": "User",
				    "user_view_type": "public",
				    "site_admin": false,
				    "name": "Andrey Litvitski",
				    "company": null,
				    "blog": "panic08.ru",
				    "location": "Minsk, Belarus",
				    "email": "andrey1010102008@gmail.com",
				    "hireable": true,
				    "bio": "🦄 java dev 16 y.o from BY",
				    "twitter_username": "litvitski",
				    "notification_email": "andrey1010102008@gmail.com",
				    "public_repos": 84,
				    "public_gists": 0,
				    "followers": 4,
				    "following": 3,
				    "created_at": "2022-12-14T04:30:37Z",
				    "updated_at": "2025-01-13T07:56:09Z"
				}
				    """;

        // Expect a GET request to be sent from the Bearer Token
        mockRestServiceServer
                .expect(requestTo("https://api.github.com/user"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andRespond(withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        // When
        GithubUser user = githubApi.getUser(accessToken);

        // Then
        assertEquals("panic08", user.login());
        assertEquals(120543954L, user.id());
        assertEquals("Andrey Litvitski", user.name());
        assertEquals(84, user.publicRepos());

        mockRestServiceServer.verify();
    }
}
