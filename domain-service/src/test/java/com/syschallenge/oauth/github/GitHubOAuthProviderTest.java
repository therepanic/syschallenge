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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.api.GitHubApi;
import com.syschallenge.shared.api.payload.response.GitHubUser;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class GitHubOAuthProviderTest {
    @Mock private GitHubOAuthApi githubOAuthApi;

    @Mock private GitHubApi githubApi;

    @Mock private GitHubOAuthProperty properties;

    @InjectMocks private GitHubOAuthProvider githubOAuthProvider;

    private final String testCode = "test_code";
    private final String testAccessTokenResponse =
            "access_token=test_access_token&other_param=value";
    private final String testAccessToken = "test_access_token";

    @Test
    void extractUser_ValidCode_ReturnsUserInfo() {
        GitHubUser testGitHubUser =
                new GitHubUser(
                        "testuser", // login
                        123, // id
                        "node_id_example", // nodeId
                        "http://avatar.url", // avatarUrl
                        "", // gravatarId
                        "", // url
                        "", // htmlUrl
                        "", // followersUrl
                        "", // followingUrl
                        "", // gistsUrl
                        "", // starredUrl
                        "", // subscriptionsUrl
                        "", // organizationsUrl
                        "", // reposUrl
                        "", // eventsUrl
                        "", // receivedEventsUrl
                        "User", // type
                        "", // userViewType
                        false, // siteAdmin
                        "Test User", // name
                        "", // company
                        "", // blog
                        "", // location
                        "test@example.com", // email
                        false, // hireable
                        "Developer", // bio
                        "", // twitterUsername
                        "", // notificationEmail
                        10, // publicRepos
                        5, // publicGists
                        100, // followers
                        50, // following
                        ZonedDateTime.now(), // createdAt
                        ZonedDateTime.now() // updatedAt
                        );

        when(properties.clientId()).thenReturn("client_id");
        when(properties.clientSecret()).thenReturn("client_secret");
        when(properties.redirectUri()).thenReturn("redirect_uri");
        when(githubOAuthApi.requestToken(any())).thenReturn(testAccessTokenResponse);
        when(githubApi.getUser(testAccessToken)).thenReturn(testGitHubUser);

        // Act
        OAuthUserInfo userInfo = githubOAuthProvider.extractUser(testCode);

        // Assert
        assertThat(userInfo)
                .hasFieldOrPropertyWithValue("providerUserId", "123")
                .hasFieldOrPropertyWithValue("email", "test@example.com");
    }
}
