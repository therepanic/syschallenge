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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.syschallenge.shared.security.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Security configuration class for setting up Spring Security
 *
 * @author panic08
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AccessDeniedHandler accessDeniedHandler;
    private final BaseAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Configures the security filter chain for HTTP requests
     *
     * @param http HttpSecurity object to configure
     * @return configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .exceptionHandling(
                        exception ->
                                exception
                                        .authenticationEntryPoint(this.authenticationEntryPoint)
                                        .accessDeniedHandler(this.accessDeniedHandler))
                .authorizeHttpRequests(
                        auth -> {
                            auth.requestMatchers("/api/v1/auth/social")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/user/{id}/occupation")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/user/{id}/photo")
                                    .permitAll()
                                    .requestMatchers("/api/v1/company/all")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/company")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/company/{id}")
                                    .permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/v1/topic/all")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated();
                        })
                .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides an AuthenticationManager bean for authentication
     *
     * @param authConfig AuthenticationConfiguration object
     * @return configured AuthenticationManager
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
