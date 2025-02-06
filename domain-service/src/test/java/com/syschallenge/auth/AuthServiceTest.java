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

import com.syschallenge.auth.model.Me;
import com.syschallenge.auth.payload.response.AuthResponse;
import com.syschallenge.oauth.OAuthProvider;
import com.syschallenge.oauth.OAuthProviderFactory;
import com.syschallenge.oauth.OAuthType;
import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.security.UserDetails;
import com.syschallenge.shared.security.jwt.JwtUtil;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.repository.UserBasicInfoRepository;
import com.syschallenge.user.repository.UserLinkedSocialRepository;
import com.syschallenge.user.repository.UserRepository;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserBasicInfo;
import com.syschallenge.user.model.UserLinkedSocial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private OAuthProviderFactory providerFactory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBasicInfoRepository userBasicInfoRepository;

    @Mock
    private UserLinkedSocialRepository userLinkedSocialRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private final String code = "authCode";
    private final OAuthType type = OAuthType.GOOGLE;
    private final String providerUserId = "providerUserId123";
    private final String email = "user@example.com";
    private final String username = "username123";
    private final String jwtToken = "jwt-token";

    private OAuthProvider oAuthProvider;
    private OAuthUserInfo oAuthUserInfo;

    @BeforeEach
    void setUp() {
        oAuthUserInfo = new OAuthUserInfo(providerUserId, email, username);
        oAuthProvider = mock(OAuthProvider.class);
        lenient().when(oAuthProvider.extractUser(code)).thenReturn(oAuthUserInfo);
        lenient().when(providerFactory.getProvider(type)).thenReturn(oAuthProvider);
        lenient().when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(jwtToken);
    }

    @Test
    void testAuthBySocial_UserExists() {
        // arrange
        UUID existingUserId = UUID.randomUUID();
        User existingUser = User.builder()
                .id(existingUserId)
                .email(email)
                .username(username)
                .role(UserRole.DEFAULT)
                .registeredAt(LocalDateTime.now())
                .build();

        when(userLinkedSocialRepository.existsByVerification(providerUserId)).thenReturn(true);
        when(userLinkedSocialRepository.findUserIdByVerification(providerUserId)).thenReturn(existingUserId);
        when(userRepository.findById(existingUserId)).thenReturn(existingUser);

        // act
        AuthResponse response = authService.authBySocial(type, code);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.jwtToken()).isEqualTo(jwtToken);
        verify(userRepository, never()).save(any(User.class));
        verify(userBasicInfoRepository, never()).save(any(UserBasicInfo.class));
        verify(userLinkedSocialRepository, times(1)).existsByVerification(providerUserId);
        verify(userLinkedSocialRepository, times(1)).findUserIdByVerification(providerUserId);
    }

    @Test
    void testAuthBySocial_NewUser() {
        // arrange
        when(userLinkedSocialRepository.existsByVerification(providerUserId)).thenReturn(false);

        UUID newUserId = UUID.randomUUID();
        User savedUser = User.builder()
                .id(newUserId)
                .email(email)
                .username(username)
                .role(UserRole.DEFAULT)
                .registeredAt(LocalDateTime.now())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // act
        AuthResponse response = authService.authBySocial(type, code);

        // assert
        assertThat(response).isNotNull();
        assertThat(response.jwtToken()).isEqualTo(jwtToken);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userBasicInfoRepository, times(1)).save(any(UserBasicInfo.class));
        verify(userLinkedSocialRepository, times(1)).save(any(UserLinkedSocial.class));
    }

    @Test
    void testMe() {
        // arrange
        UUID userId = UUID.randomUUID();
        String foundUsername = "foundUsername";
        String foundName = "foundName";
        when(userRepository.findUsernameById(userId)).thenReturn(foundUsername);
        when(userBasicInfoRepository.findNameByUserId(userId)).thenReturn(foundName);

        // act
        Me me = authService.me(userId);

        // assert
        assertThat(me).isNotNull();
        assertThat(me.id()).isEqualTo(userId);
        assertThat(me.username()).isEqualTo(foundUsername);
        assertThat(me.name()).isEqualTo(foundName);
    }
}
