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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Entry point for handling unauthorized access
 *
 * @author therepanic
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class BaseAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    /**
     * Commences an authentication scheme, returning an unauthorized response
     *
     * @param request incoming HTTP request
     * @param response outgoing HTTP response
     * @param authException authentication exception
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> body =
                Map.ofEntries(
                        Map.entry("error", "Unauthorized"),
                        Map.entry("message", authException.getMessage()),
                        Map.entry("path", request.getServletPath()));
        this.mapper.writeValue(response.getOutputStream(), body);
    }
}
