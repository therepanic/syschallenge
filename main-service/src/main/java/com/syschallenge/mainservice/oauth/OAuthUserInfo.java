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

/**
 * Represents user information obtained from OAuth provider authentication
 *
 * @param providerUserId unique user identifier from OAuth provider
 * @param email user's email address
 *
 * @author panic08
 * @since 1.0.0
 */
public record OAuthUserInfo(String providerUserId, String email) {
}
