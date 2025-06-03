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

package com.syschallenge.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.syschallenge.shared.exception.PermissionDeniedException;
import com.syschallenge.user.dto.UserOccupationDto;
import com.syschallenge.user.mapper.UserOccupationToUserOccupationDtoMapper;
import com.syschallenge.user.model.UserOccupation;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.payload.request.CreateOccupationRequest;
import com.syschallenge.user.payload.request.UpdateOccupationRequest;
import com.syschallenge.user.repository.UserOccupationRepository;
import com.syschallenge.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for handling user occupation related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserOccupationService {

    private final UserRepository userRepository;
    private final UserOccupationRepository userOccupationRepository;
    private final UserOccupationToUserOccupationDtoMapper userOccupationToUserOccupationDtoMapper;

    /**
     * Get a user's occupation details
     *
     * @param id the user ID whose occupation is being got
     * @return a DTO containing the got user occupation information
     */
    public UserOccupationDto get(UUID id) {
        return this.userOccupationToUserOccupationDtoMapper.userOccupationToUserOccupationDto(
                this.userOccupationRepository.findByUserId(id));
    }

    /**
     * Creates a user's occupation details
     *
     * @param id the user ID whose occupation is being created
     * @param request the occupation create details
     * @param principalId the authenticated user ID performing the operation
     * @return a DTO containing the created user occupation information
     * @throws PermissionDeniedException if the user does not have permission to update this
     *     occupation
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserOccupationDto create(UUID id, CreateOccupationRequest request, UUID principalId)
            throws PermissionDeniedException {
        if (!id.equals(principalId)
                && this.userRepository.findRoleById(principalId).equals(UserRole.DEFAULT)) {
            throw new PermissionDeniedException("You can only create your own occupation.");
        }
        return this.userOccupationToUserOccupationDtoMapper.userOccupationToUserOccupationDto(
                this.userOccupationRepository.save(
                        UserOccupation.builder()
                                .userId(id)
                                .title(request.title())
                                .company(request.company())
                                .startDate(request.startDate())
                                .endDate(request.endDate())
                                .build()));
    }

    /**
     * Updates a user's occupation details
     *
     * @param id the user ID whose occupation is being updated
     * @param request the occupation update details
     * @param principalId the authenticated user ID performing the operation
     * @return a DTO containing the updated user occupation information
     * @throws PermissionDeniedException if the user does not have permission to save this
     *     occupation
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserOccupationDto update(UUID id, UpdateOccupationRequest request, UUID principalId)
            throws PermissionDeniedException {
        if (!id.equals(principalId)
                && this.userRepository.findRoleById(principalId).equals(UserRole.DEFAULT)) {
            throw new PermissionDeniedException("You can only update your own occupation.");
        }
        return this.userOccupationToUserOccupationDtoMapper.userOccupationToUserOccupationDto(
                this.userOccupationRepository.updateByUserId(
                        UserOccupation.builder()
                                .userId(id)
                                .title(request.title())
                                .company(request.company())
                                .startDate(request.startDate())
                                .endDate(request.endDate())
                                .build()));
    }

    /**
     * Deletes a user's occupation details
     *
     * @param id the user ID whose occupation is being deleted
     * @param principalId the authenticated user ID performing the operation
     * @throws PermissionDeniedException if the user does not have permission to delete this
     *     occupation
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id, UUID principalId) throws PermissionDeniedException {
        if (!id.equals(principalId)
                && this.userRepository.findRoleById(principalId).equals(UserRole.DEFAULT)) {
            throw new PermissionDeniedException("You can only delete your own occupation.");
        }
        this.userOccupationRepository.deleteByUserId(id);
    }
}
