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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.syschallenge.shared.exception.PermissionDeniedException;
import com.syschallenge.user.dto.UserOccupationDto;
import com.syschallenge.user.mapper.UserOccupationToUserOccupationDtoMapper;
import com.syschallenge.user.model.UserOccupation;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.payload.request.UpdateOccupationRequest;
import com.syschallenge.user.repository.UserOccupationRepository;
import com.syschallenge.user.repository.UserRepository;
import com.syschallenge.user.service.UserOccupationService;

/**
 * @author therepanic
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class UserOccupationServiceTest {

    @Mock private UserOccupationRepository userOccupationRepository;

    @Mock private UserOccupationToUserOccupationDtoMapper userOccupationToUserOccupationDtoMapper;

    @Mock private UserRepository userRepository;

    @InjectMocks private UserOccupationService userOccupationService;

    @Test
    void update_throwsPermissionDeniedException_whenIdNotEqualsPrincipalId() {
        UUID id = UUID.randomUUID();
        UUID principalId = UUID.randomUUID();
        UpdateOccupationRequest request =
                new UpdateOccupationRequest(
                        "Acme Corp",
                        "Software Engineer",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(30));

        when(userRepository.findRoleById(principalId)).thenReturn(UserRole.DEFAULT);

        PermissionDeniedException exception =
                assertThrows(
                        PermissionDeniedException.class,
                        () -> userOccupationService.update(id, request, principalId));
        assertEquals("You can only update your own occupation.", exception.getMessage());
    }

    @Test
    void update_updatesAndReturnsUserOccupationDto_whenIdNotEqualToPrincipalId() {
        UUID id = UUID.randomUUID();
        UUID principalId = UUID.randomUUID();
        UpdateOccupationRequest request =
                new UpdateOccupationRequest(
                        "Acme Corp",
                        "Software Engineer",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(30));

        when(userRepository.findRoleById(principalId)).thenReturn(UserRole.ADMIN);

        UserOccupation occupationToUpdate =
                UserOccupation.builder()
                        .userId(id)
                        .title(request.title())
                        .company(request.company())
                        .startDate(request.startDate())
                        .endDate(request.endDate())
                        .build();

        UserOccupation updatedOccupation = occupationToUpdate;

        UserOccupationDto expectedDto =
                new UserOccupationDto(
                        id,
                        request.company(),
                        request.title(),
                        request.startDate(),
                        request.endDate());

        when(userOccupationRepository.updateByUserId(any(UserOccupation.class)))
                .thenReturn(updatedOccupation);
        when(userOccupationToUserOccupationDtoMapper.userOccupationToUserOccupationDto(
                        updatedOccupation))
                .thenReturn(expectedDto);

        UserOccupationDto actualDto = userOccupationService.update(id, request, principalId);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);

        verify(userOccupationRepository)
                .updateByUserId(
                        argThat(
                                occupation ->
                                        occupation.getUserId().equals(id)
                                                && occupation.getTitle().equals(request.title())
                                                && occupation.getCompany().equals(request.company())
                                                && occupation
                                                        .getStartDate()
                                                        .equals(request.startDate())
                                                && occupation
                                                        .getEndDate()
                                                        .equals(request.endDate())));
        verify(userOccupationToUserOccupationDtoMapper)
                .userOccupationToUserOccupationDto(updatedOccupation);
    }
}
