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

package com.syschallenge.auth;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syschallenge.auth.model.Me;
import com.syschallenge.auth.payload.response.AuthResponse;
import com.syschallenge.oauth.OAuthType;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling authentication requests
 *
 * @author therepanic
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for OAuth authentication
     *
     * @param type social type for OAuth
     * @param code authorization code received from OAuth
     * @return auth data
     */
    @PostMapping("/social")
    public AuthResponse authBySocial(
            @RequestParam("type") OAuthType type, @RequestParam("code") String code) {
        return this.authService.authBySocial(type, code);
    }

    /**
     * Endpoint for retrieve the information about the authenticated user
     *
     * @param auth the authentication token, which contains the user ID as the principal name
     * @return user information (Me) for the authenticated user
     */
    @GetMapping("/me")
    public Me me(UsernamePasswordAuthenticationToken auth) {
        return this.authService.me(UUID.fromString(auth.getName()));
    }
}
