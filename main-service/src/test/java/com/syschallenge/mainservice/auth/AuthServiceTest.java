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

import com.syschallenge.mainservice.auth.response.AuthResponse;
import com.syschallenge.mainservice.oauth.OAuthProvider;
import com.syschallenge.mainservice.oauth.OAuthProviderFactory;
import com.syschallenge.mainservice.oauth.OAuthType;
import com.syschallenge.mainservice.oauth.OAuthUserInfo;
import com.syschallenge.mainservice.shared.security.jwt.JwtUtil;
import com.syschallenge.mainservice.user.UserLinkedSocialRepository;
import com.syschallenge.mainservice.user.UserRepository;
import com.syschallenge.mainservice.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author panic08
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OAuthProviderFactory providerFactory;

    @Mock
    private OAuthProvider oAuthProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLinkedSocialRepository userLinkedSocialRepository;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        when(providerFactory.getProvider(OAuthType.GOOGLE))
                .thenReturn(oAuthProvider);
    }

    private final String testCode = "test_code";
    private final String testUserId = "google_user_123";
    private final String testEmail = "test@example.com";
    private final UUID uuid = UUID.randomUUID();
    private final String testToken = "test.jwt.token";

    @Test
    void authByGoogle_ExistingUser_ReturnsToken() {
        // Arrange
        OAuthUserInfo userInfo = new OAuthUserInfo(
                testUserId,
                testEmail
        );

        User mockUser = User.builder()
                .id(uuid)
                .email(testEmail)
                .registeredAt(LocalDateTime.now())
                .build();

        when(providerFactory.getProvider(OAuthType.GOOGLE)).thenReturn(oAuthProvider);
        when(oAuthProvider.extractUser(testCode)).thenReturn(userInfo);
        when(userLinkedSocialRepository.existsByVerification(testUserId)).thenReturn(true);
        when(userLinkedSocialRepository.findUserIdByVerification(testUserId)).thenReturn(uuid);
        when(userRepository.findById(uuid)).thenReturn(mockUser);
        when(jwtUtil.generateToken(any())).thenReturn(testToken);

        // Act
        AuthResponse response = authService.authBySocial(OAuthType.GOOGLE, testCode);

        // Assert
        assertEquals(testToken, response.jwtToken());
        verify(providerFactory).getProvider(OAuthType.GOOGLE);
        verify(userLinkedSocialRepository).existsByVerification(testUserId);
        verify(userRepository).findById(uuid);
    }

    @Test
    void authByGoogle_NewUser_CreatesUserAndSocialLink() {
        // Arrange
        OAuthUserInfo userInfo = new OAuthUserInfo(
                testUserId,
                testEmail
        );

        User newUser = User.builder()
                .id(uuid)
                .email(testEmail)
                .registeredAt(LocalDateTime.now())
                .build();

        when(providerFactory.getProvider(OAuthType.GOOGLE)).thenReturn(oAuthProvider);
        when(oAuthProvider.extractUser(testCode)).thenReturn(userInfo);
        when(userLinkedSocialRepository.existsByVerification(testUserId)).thenReturn(false);
        when(userRepository.save(any())).thenReturn(newUser);
        when(jwtUtil.generateToken(any())).thenReturn(testToken);

        // Act
        AuthResponse response = authService.authBySocial(OAuthType.GOOGLE, testCode);

        // Assert
        assertEquals(testToken, response.jwtToken());

        // Verify user creation
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(testEmail, userCaptor.getValue().getEmail());

        // Verify async social link creation
        verify(userLinkedSocialRepository, timeout(1000)).save(any());
    }
}
