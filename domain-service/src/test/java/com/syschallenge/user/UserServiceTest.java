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

package com.syschallenge.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.service.FileStorageService;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserBasicInfo;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.payload.response.PhotoResponse;
import com.syschallenge.user.repository.UserBasicInfoRepository;
import com.syschallenge.user.repository.UserPhotoRepository;
import com.syschallenge.user.repository.UserRepository;
import com.syschallenge.user.service.UserService;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserPhotoRepository userPhotoRepository;

	@Mock
	private UserBasicInfoRepository userBasicInfoRepository;

	@Mock
	private FileStorageService storageService;

	@InjectMocks
	private UserService userService;

	@Test
	public void testCreateUser() {
		// arrange
		OAuthUserInfo userInfo = new OAuthUserInfo("providerId", "username", "user@example.com", null);
		when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(userBasicInfoRepository.save(any(UserBasicInfo.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		// act
		User result = userService.create(userInfo);

		// assert
		assertEquals("user@example.com", result.getEmail());
		assertEquals("username", result.getUsername());
		assertEquals(UserRole.DEFAULT, result.getRole());
		assertNotNull(result.getRegisteredAt());

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(userCaptor.capture());
		User capturedUser = userCaptor.getValue();
		assertEquals("user@example.com", capturedUser.getEmail());
		assertEquals("username", capturedUser.getUsername());
		assertEquals(UserRole.DEFAULT, capturedUser.getRole());

		ArgumentCaptor<UserBasicInfo> infoCaptor = ArgumentCaptor.forClass(UserBasicInfo.class);
		verify(userBasicInfoRepository).save(infoCaptor.capture());
		UserBasicInfo capturedInfo = infoCaptor.getValue();
		assertEquals(capturedUser.getId(), capturedInfo.getUserId());
		assertEquals("username", capturedInfo.getName());
	}

	@Test
	public void testGetUsernameById() {
		// arrange
		UUID id = UUID.randomUUID();
		when(userRepository.findUsernameById(id)).thenReturn("username");

		// act
		String username = userService.getUsernameById(id);

		// assert
		assertEquals("username", username);
	}

	@Test
	public void testGetById() {
		// arrange
		UUID id = UUID.randomUUID();
		User user = User.builder().id(id).username("username").build();
		when(userRepository.findById(id)).thenReturn(user);

		// act
		User result = userService.getById(id);

		// assert
		assertEquals(user, result);
	}

	@Test
	public void testGetPhoto_WhenPhotoExists() {
		// arrange
		UUID userId = UUID.randomUUID();
		String objectKey = "photo-key";
		byte[] photoData = new byte[] { 1, 2, 3 };

		when(userPhotoRepository.findObjectKeyByUserId(userId)).thenReturn(objectKey);
		when(storageService.downloadFile("users-photo", objectKey)).thenReturn(photoData);

		// act
		PhotoResponse response = userService.getPhoto(userId);

		// assert
		assertNotNull(response);
		assertArrayEquals(photoData, response.photo());
		assertEquals(objectKey, response.photoFileName());
	}

	@Test
	public void testGetPhoto_WhenPhotoDoesNotExist() {
		// arrange
		UUID userId = UUID.randomUUID();
		when(userPhotoRepository.findObjectKeyByUserId(userId)).thenReturn(null);

		// act
		PhotoResponse response = userService.getPhoto(userId);

		// assert
		assertNull(response);
	}

}
