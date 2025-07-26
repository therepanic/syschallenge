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

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syschallenge.user.dto.UserOccupationDto;
import com.syschallenge.user.payload.request.CreateOccupationRequest;
import com.syschallenge.user.payload.request.UpdateOccupationRequest;
import com.syschallenge.user.service.UserOccupationService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling user occupation-related operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserOccupationController {

	private final UserOccupationService userOccupationService;

	/**
	 * Endpoint for retrieving the occupation information for a specific user
	 * @param id the UUID of the user to fetch occupation for
	 * @return the user occupation DTO containing employment details
	 */
	@GetMapping("/{id}/occupation")
	public UserOccupationDto get(@PathVariable("id") UUID id) {
		return this.userOccupationService.get(id);
	}

	/**
	 * Endpoint for creating a new occupation record for the specified user
	 * @param id the UUID of the user to create occupation for
	 * @param request the validated create occupation request payload
	 * @param auth the authentication token containing the requester's identity
	 * @return the created user occupation DTO with persisted data
	 */
	@PostMapping("/{id}/occupation")
	public UserOccupationDto create(@PathVariable("id") UUID id,
			@RequestBody @Validated CreateOccupationRequest request, UsernamePasswordAuthenticationToken auth) {
		return this.userOccupationService.create(id, request, UUID.fromString(auth.getName()));
	}

	/**
	 * Endpoint for updating existing occupation information for the specified user
	 * @param id the UUID of the user to update occupation for
	 * @param request the validated update occupation request payload
	 * @param auth the authentication token containing the requester's identity
	 * @return the updated user occupation DTO with modified data
	 */
	@PutMapping("/{id}/occupation")
	public UserOccupationDto update(@PathVariable("id") UUID id,
			@RequestBody @Validated UpdateOccupationRequest request, UsernamePasswordAuthenticationToken auth) {
		return this.userOccupationService.update(id, request, UUID.fromString(auth.getName()));
	}

	/**
	 * Endpoint for deleting occupation information for the specified user
	 * @param id the UUID of the user to delete occupation for
	 * @param auth the authentication token containing the requester's identity
	 */
	@DeleteMapping("/{id}/occupation")
	public void delete(@PathVariable("id") UUID id, UsernamePasswordAuthenticationToken auth) {
		this.userOccupationService.delete(id, UUID.fromString(auth.getName()));
	}

}
