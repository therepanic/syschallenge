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

package com.syschallenge.shared.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Entry point for handling access denial errors
 *
 * @author panic08
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class BaseAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    /**
     * Handles an access denial scenario, returning a forbidden response
     *
     * @param request incoming HTTP request
     * @param response outgoing HTTP response
     * @param accessDeniedException access denial exception
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String, Object> body =
                Map.ofEntries(
                        Map.entry("error", "Forbidden"),
                        Map.entry("message", "You don't have the rights"),
                        Map.entry("path", request.getServletPath()));
        this.mapper.writeValue(response.getOutputStream(), body);
    }
}
