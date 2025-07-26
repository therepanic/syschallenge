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

package com.syschallenge.shared.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.syschallenge.shared.exception.PermissionDeniedException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler for handling application-wide exceptions
 *
 * @author therepanic
 * @since 1.0.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles exceptions when a user attempts an action without sufficient permissions
	 * @param exception the exception thrown when permission is denied
	 * @return a standardized error response with HTTP status 403 (Forbidden)
	 */
	@ExceptionHandler(PermissionDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String, Object> handlePermissionDeniedException(HttpServletRequest request,
			PermissionDeniedException exception) {
		return Map.ofEntries(Map.entry("error", "Forbidden"), Map.entry("message", exception.getMessage()),
				Map.entry("path", request.getServletPath()));
	}

	/**
	 * Handles exceptions thrown when request validation fails.
	 *
	 * <p>
	 * This method captures validation errors from {@link MethodArgumentNotValidException}
	 * and returns the first error message encountered in a standardized error response.
	 * @param exception the exception thrown when request validation fails
	 * @return a standardized error response with HTTP status 400 (Bad Request)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleValidationException(HttpServletRequest request,
			MethodArgumentNotValidException exception) {
		String errorMessage = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.findFirst()
			.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
			.orElse("Validation error");
		return Map.ofEntries(Map.entry("error", "Forbidden"), Map.entry("message", errorMessage),
				Map.entry("path", request.getServletPath()));
	}

}
