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

package com.syschallenge.user.controller;

import com.syschallenge.user.dto.UserOccupationDto;
import com.syschallenge.user.payload.request.CreateOccupationRequest;
import com.syschallenge.user.payload.request.UpdateOccupationRequest;
import com.syschallenge.user.service.UserOccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for handling user occupation-related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserOccupationController {

    private final UserOccupationService userOccupationService;

    /**
     * Retrieves the occupation information for a specific user
     *
     * @param id UUID of the user to fetch occupation for
     * @return user occupation DTO containing employment details
     */
    @GetMapping("/{id}/occupation")
    public UserOccupationDto getOccupation(@PathVariable("id") UUID id) {
        return userOccupationService.getOccupation(id);
    }

    /**
     * Creates a new occupation record for the specified user
     *
     * @param id UUID of the user to create occupation for
     * @param request validated create occupation request payload
     * @param auth authentication token containing requester's identity
     * @return created user occupation DTO with persisted data
     */
    @PostMapping("/{id}/occupation")
    public UserOccupationDto createOccupation(@PathVariable("id") UUID id,
                                              @RequestBody @Validated CreateOccupationRequest request,
                                              UsernamePasswordAuthenticationToken auth) {
        return userOccupationService.createOccupation(id, request, UUID.fromString(auth.getName()));
    }

    /**
     * Updates existing occupation information for the specified user
     *
     * @param id UUID of the user to update occupation for
     * @param request validated update occupation request payload
     * @param auth authentication token containing requester's identity
     * @return updated user occupation DTO with modified data
     */
    @PutMapping("/{id}/occupation")
    public UserOccupationDto updateOccupation(@PathVariable("id") UUID id,
                                              @RequestBody @Validated UpdateOccupationRequest request,
                                              UsernamePasswordAuthenticationToken auth) {
        return userOccupationService.updateOccupation(id, request, UUID.fromString(auth.getName()));
    }

    /**
     * Deletes occupation information for the specified user
     *
     * @param id UUID of the user to delete occupation for
     * @param auth authentication token containing requester's identity
     */
    @DeleteMapping("/{id}/occupation")
    public void deleteOccupation(@PathVariable("id") UUID id,
                                 UsernamePasswordAuthenticationToken auth) {
        userOccupationService.deleteOccupation(id, UUID.fromString(auth.getName()));
    }

}
