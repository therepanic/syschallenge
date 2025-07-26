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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.auth.model.Me;
import com.syschallenge.auth.payload.response.AuthResponse;
import com.syschallenge.oauth.OAuthProvider;
import com.syschallenge.oauth.OAuthProviderFactory;
import com.syschallenge.oauth.OAuthType;
import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.security.UserDetails;
import com.syschallenge.shared.security.jwt.JwtUtil;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.service.UserBasicInfoService;
import com.syschallenge.user.service.UserLinkedSocialService;
import com.syschallenge.user.service.UserService;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private OAuthProviderFactory providerFactory;

	@Mock
	private UserService userService;

	@Mock
	private UserLinkedSocialService userLinkedSocialService;

	@Mock
	private UserBasicInfoService userBasicInfoService;

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private OAuthProvider oAuthProvider;

	@InjectMocks
	private AuthService authService;

	@Test
	public void testAuthBySocial_existingUser() {
		// Arrange
		OAuthType type = OAuthType.GOOGLE;
		String code = "authCode";
		OAuthUserInfo userInfo = mock(OAuthUserInfo.class);
		when(userInfo.providerUserId()).thenReturn("providerUserId123");
		when(providerFactory.getProvider(type)).thenReturn(oAuthProvider);
		when(oAuthProvider.extractUser(code)).thenReturn(userInfo);
		when(userLinkedSocialService.existsByVerification("providerUserId123")).thenReturn(true);
		UUID existingUserId = UUID.randomUUID();
		when(userLinkedSocialService.getUserIdByVerification("providerUserId123")).thenReturn(existingUserId);
		User existingUser = User.builder()
			.id(existingUserId)
			.username("existingUser")
			.email("existing@example.com")
			.password("password")
			.role(UserRole.DEFAULT)
			.registeredAt(LocalDateTime.now())
			.build();
		when(userService.getById(existingUserId)).thenReturn(existingUser);
		String jwtToken = "jwt-token-existing";
		when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

		// Act
		AuthResponse response = authService.authBySocial(type, code);

		// Assert
		assertEquals(jwtToken, response.jwtToken());
		verify(userService, never()).create(any());
		verify(userLinkedSocialService, never()).create(any(), any(), any());
	}

	@Test
	public void testAuthBySocial_newUser() {
		// Arrange
		OAuthType type = OAuthType.GOOGLE;
		String code = "authCodeNew";
		OAuthUserInfo userInfo = mock(OAuthUserInfo.class);
		when(userInfo.providerUserId()).thenReturn("newProviderUserId456");
		when(providerFactory.getProvider(type)).thenReturn(oAuthProvider);
		when(oAuthProvider.extractUser(code)).thenReturn(userInfo);
		when(userLinkedSocialService.existsByVerification("newProviderUserId456")).thenReturn(false);
		UUID newUserId = UUID.randomUUID();
		User newUser = User.builder()
			.id(newUserId)
			.username("newUser")
			.email("new@example.com")
			.password("password")
			.role(UserRole.DEFAULT)
			.registeredAt(LocalDateTime.now())
			.build();
		when(userService.create(userInfo)).thenReturn(newUser);
		String jwtToken = "jwt-token-new";
		when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

		// Act
		AuthResponse response = authService.authBySocial(type, code);

		// Assert
		assertEquals(jwtToken, response.jwtToken());
		verify(userService, times(1)).create(userInfo);
		verify(userLinkedSocialService, times(1)).create(newUserId, type, "newProviderUserId456");
	}

	@Test
	public void testMe() {
		// Arrange
		UUID userId = UUID.randomUUID();
		String username = "testUser";
		String name = "Test User";
		when(userService.getUsernameById(userId)).thenReturn(username);
		when(userBasicInfoService.getNameByUserId(userId)).thenReturn(name);

		// Act
		Me meResponse = authService.me(userId);

		// Assert
		assertEquals(userId, meResponse.id());
		assertEquals(username, meResponse.username());
		assertEquals(name, meResponse.name());
	}

}
