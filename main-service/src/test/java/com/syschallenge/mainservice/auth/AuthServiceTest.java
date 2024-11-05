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

package com.syschallenge.mainservice.auth;

import com.syschallenge.mainservice.auth.response.GoogleAuthResponse;
import com.syschallenge.mainservice.oauth.GoogleOAuthV4Service;
import com.syschallenge.mainservice.oauth.model.GoogleOAuthIdTokenUser;
import com.syschallenge.mainservice.property.GoogleOAuthProperty;
import com.syschallenge.mainservice.security.UserDetails;
import com.syschallenge.mainservice.security.jwt.JwtUtil;
import com.syschallenge.mainservice.user.UserLinkedSocialRepository;
import com.syschallenge.mainservice.user.UserRepository;
import com.syschallenge.mainservice.user.model.User;
import com.syschallenge.mainservice.user.model.UserLinkedSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author panic08
 * @since 1.0.0
 */
class AuthServiceTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GoogleOAuthV4Service googleOAuthService;

    @Mock
    private GoogleOAuthProperty googleOAuthProperty;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLinkedSocialRepository userLinkedSocialRepository;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthByGoogleUserExists() {
        String code = "authCode";
        String userId = "googleUserId";

        UUID existingUserId = UUID.randomUUID();

        String expectedJwtToken = "jwtToken";

        GoogleOAuthIdTokenUser googleUser = new GoogleOAuthIdTokenUser(userId, "email@example.com", true, "test",
                "test", "test");

        when(googleOAuthProperty.getClientId()).thenReturn("clientId");
        when(googleOAuthProperty.getClientSecret()).thenReturn("clientSecret");
        when(googleOAuthProperty.getRedirectUri()).thenReturn("redirectUri");
        when(googleOAuthProperty.getGrantType()).thenReturn("authorization_code");

        when(googleOAuthService.extractGoogleOAuthIdTokenUser(any(), any(), any(), eq(code), any())).thenReturn(googleUser);
        when(userLinkedSocialRepository.existsByVerification(userId)).thenReturn(true);
        when(userLinkedSocialRepository.findUserIdByVerification(userId)).thenReturn(existingUserId);

        User existingUser = User.builder()
                .id(existingUserId)
                .email("email@example.com")
                .registeredAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(existingUserId)).thenReturn(existingUser);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(expectedJwtToken);

        GoogleAuthResponse response = authService.authByGoogle(code);

        assertEquals(expectedJwtToken, response.jwtToken());
    }

    @Test
    public void testAuthByGoogleUserNotExists() {
        String code = "authCode";
        String userId = "googleUserId";

        UUID newUserId = UUID.randomUUID();

        String expectedJwtToken = "jwtToken";

        GoogleOAuthIdTokenUser googleUser = new GoogleOAuthIdTokenUser(userId, "newuser@example.com", true, "test",
                "test", "test");

        when(googleOAuthProperty.getClientId()).thenReturn("clientId");
        when(googleOAuthProperty.getClientSecret()).thenReturn("clientSecret");
        when(googleOAuthProperty.getRedirectUri()).thenReturn("redirectUri");
        when(googleOAuthProperty.getGrantType()).thenReturn("authorization_code");

        when(googleOAuthService.extractGoogleOAuthIdTokenUser(any(), any(), any(), eq(code), any())).thenReturn(googleUser);
        when(userLinkedSocialRepository.existsByVerification(userId)).thenReturn(false);

        User newUser = User.builder().id(newUserId).email("newuser@example.com").registeredAt(LocalDateTime.now()).build();
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(expectedJwtToken);

        GoogleAuthResponse response = authService.authByGoogle(code);

        assertEquals(expectedJwtToken, response.jwtToken());
        verify(userLinkedSocialRepository, times(1)).save(any(UserLinkedSocial.class));
    }
}
