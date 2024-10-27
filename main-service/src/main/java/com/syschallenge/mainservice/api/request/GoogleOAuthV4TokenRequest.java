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

package com.syschallenge.mainservice.api.request;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents a request payload for Google OAuth V4 token request
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Builder
public class GoogleOAuthV4TokenRequest {
    private String clientId;

    private String clientSecret;

    private String redirectUri;

    private String code;

    private String grantType;
}
