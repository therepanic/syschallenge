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

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * A record representing user details for authentication
 *
 * @author panic08
 * @since 1.0.0
 */
public record UserDetails(UUID id, Collection<? extends GrantedAuthority> authorities) implements org.springframework.security.core.userdetails.UserDetails {

    /**
     * Returns the authorities granted to the user
     *
     * @return the authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Returns the password used for authentication
     *
     * @return empty string as this implementation does not use a password
     */
    @Override
    public String getPassword() {
        return "";
    }

    /**
     * Returns the username for the user
     *
     * @return user's unique identifier as a string
     */
    @Override
    public String getUsername() {
        return this.id.toString();
    }
}
