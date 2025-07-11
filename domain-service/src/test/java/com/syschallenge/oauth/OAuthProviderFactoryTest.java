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

package com.syschallenge.oauth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.oauth.github.GitHubOAuthProvider;
import com.syschallenge.oauth.google.GoogleOAuthProvider;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class OAuthProviderFactoryTest {
    @Mock private GoogleOAuthProvider googleProvider;
    @Mock private GitHubOAuthProvider githubOAuthProvider;

    @InjectMocks private OAuthProviderFactory factory;

    @Test
    void getProvider_GoogleType_ReturnsGoogleProvider() {
        // Act
        OAuthProvider result = factory.getProvider(OAuthType.GOOGLE);

        // Assert
        assertNotNull(result);
        assertInstanceOf(GoogleOAuthProvider.class, result);
        assertEquals(googleProvider, result);
    }

    @Test
    void getProvider_GitHubType_ReturnsGitHubProvider() {
        // Act
        OAuthProvider result = factory.getProvider(OAuthType.GITHUB);

        // Assert
        assertNotNull(result);
        assertInstanceOf(GitHubOAuthProvider.class, result);
        assertEquals(githubOAuthProvider, result);
    }
}
