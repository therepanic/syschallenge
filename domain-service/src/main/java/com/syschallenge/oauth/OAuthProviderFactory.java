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

import com.syschallenge.oauth.github.GithubOAuthProvider;
import com.syschallenge.oauth.google.GoogleOAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Factory class for obtaining OAuth provider implementations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class OAuthProviderFactory {

    private final GoogleOAuthProvider googleProvider;
    private final GithubOAuthProvider githubProvider;

    /**
     * Returns OAuth provider implementation for specified type
     *
     * @param type OAuth provider type
     * @return configured OAuth provider implementation
     * @throws IllegalArgumentException if provider type is not supported
     */
    public OAuthProvider getProvider(OAuthType type) {
        return switch (type) {
            case GOOGLE -> googleProvider;
            case GITHUB -> githubProvider;
            default -> throw new IllegalArgumentException("Unsupported OAuth provider");
        };
    }

}
