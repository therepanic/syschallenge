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

package com.syschallenge.company;

import com.syschallenge.company.exception.CompanyAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Company exception handler for handling company-kind exceptions
 *
 * @author panic08
 * @since 1.0.0
 */
@RestControllerAdvice
public class CompanyExceptionHandler {

    /**
     * Handles exceptions thrown when attempting to create a company which already exists
     *
     * @param exception the exception thrown when a company which already exists
     * @return a standardized error response with HTTP status 409 (Conflict)
     */
    @ExceptionHandler(CompanyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleCompanyAlreadyExistsException(HttpServletRequest request, CompanyAlreadyExistsException exception) {
        return Map.ofEntries(
                Map.entry("error", "Conflict"),
                Map.entry("message", exception.getMessage()),
                Map.entry("path", request.getServletPath())
        );
    }
}
