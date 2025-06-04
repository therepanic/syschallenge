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

package com.syschallenge.user.payload.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

/**
 * Represents a request payload for creating a user's occupation
 *
 * @author therepanic
 * @since 1.0.0
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateOccupationRequest(
        @NotBlank(message = "Company name is required")
                @Size(max = 60, message = "Company name must be less than 60 characters")
                String company,
        @NotBlank(message = "Title is required")
                @Size(max = 60, message = "Title must be less than 60 characters")
                String title,
        @NotNull(message = "Start date is required")
                @PastOrPresent(message = "Start date must be in the present or past")
                LocalDateTime startDate,
        @PastOrPresent(message = "End date must be in the present or future")
                LocalDateTime endDate) {}
