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

package com.syschallenge.shared.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.syschallenge.shared.security.UserDetails;

/**
 * @author therepanic
 * @since 1.0.0
 */
class JwtUtilTest {

	private JwtUtil jwtUtil;

	@Mock
	private UserDetails userDetails;

	@BeforeEach
	void setUp() {
		this.jwtUtil = new JwtUtil();

		MockitoAnnotations.openMocks(this);
		when(userDetails.getUsername()).thenReturn("test-UUID");

		ReflectionTestUtils.setField(jwtUtil, "SECRET", "Z3dlZ2VHR0dHRVdnd2Vhc2Rhc2RzYWRhc2Rhc2RzYWQ=");
	}

	@Test
	void testGenerateToken() {
		String token = jwtUtil.generateToken(userDetails);
		assertNotNull(token);
		assertEquals("test-UUID", jwtUtil.extractIdFromToken(token));
	}

	@Test
	void testExtractExpiration() {
		String token = jwtUtil.generateToken(userDetails);
		Date expiration = jwtUtil.extractExpiration(token);
		assertNotNull(expiration);
		assertTrue(expiration.after(new Date()));
	}

	@Test
	void testIsTokenValid() {
		String token = jwtUtil.generateToken(userDetails);
		assertTrue(jwtUtil.isTokenValid(token));
	}

	@Test
	void testIsTokenExpired() {
		String token = jwtUtil.generateToken(userDetails);
		assertFalse(jwtUtil.isTokenExpired(token));
	}

	@Test
	void testExtractIdFromToken() {
		String token = jwtUtil.generateToken(userDetails);
		assertEquals("test-UUID", jwtUtil.extractIdFromToken(token));
	}

}
