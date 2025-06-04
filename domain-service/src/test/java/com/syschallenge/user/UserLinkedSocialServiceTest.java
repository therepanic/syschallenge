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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.syschallenge.oauth.OAuthType;
import com.syschallenge.user.model.UserLinkedSocial;
import com.syschallenge.user.model.UserLinkedSocialType;
import com.syschallenge.user.repository.UserLinkedSocialRepository;
import com.syschallenge.user.service.UserLinkedSocialService;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class UserLinkedSocialServiceTest {
    @Mock private UserLinkedSocialRepository userLinkedSocialRepository;

    @InjectMocks private UserLinkedSocialService userLinkedSocialService;

    @Test
    public void testCreateUserLinkedSocial() {
        // arrange
        UUID userId = UUID.randomUUID();
        OAuthType type = OAuthType.GOOGLE;
        String verification = "verificationCode";
        UserLinkedSocial savedEntity =
                UserLinkedSocial.builder()
                        .id(UUID.randomUUID())
                        .userId(userId)
                        .type(UserLinkedSocialType.valueOf(type.name()))
                        .verification(verification)
                        .build();
        when(userLinkedSocialRepository.save(any(UserLinkedSocial.class))).thenReturn(savedEntity);

        // act
        UserLinkedSocial result = userLinkedSocialService.create(userId, type, verification);

        // assert
        ArgumentCaptor<UserLinkedSocial> captor = ArgumentCaptor.forClass(UserLinkedSocial.class);
        verify(userLinkedSocialRepository).save(captor.capture());
        UserLinkedSocial capturedEntity = captor.getValue();
        assertEquals(userId, capturedEntity.getUserId());
        assertEquals(UserLinkedSocialType.valueOf(type.name()), capturedEntity.getType());
        assertEquals(verification, capturedEntity.getVerification());
        assertEquals(savedEntity, result);
    }

    @Test
    public void testGetUserIdByVerification() {
        // arrange
        String verification = "testVerification";
        UUID expectedUserId = UUID.randomUUID();
        when(userLinkedSocialRepository.findUserIdByVerification(verification))
                .thenReturn(expectedUserId);

        // act
        UUID userId = userLinkedSocialService.getUserIdByVerification(verification);

        // assert
        assertEquals(expectedUserId, userId);
    }

    @Test
    public void testExistsByVerification() {
        // arrange
        String verification = "existsVerification";
        when(userLinkedSocialRepository.existsByVerification(verification)).thenReturn(true);

        // act
        boolean exists = userLinkedSocialService.existsByVerification(verification);

        // assert
        assertTrue(exists);
    }
}
