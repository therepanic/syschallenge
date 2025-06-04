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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.syschallenge.shared.security.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Utility class for generating and validating JWT tokens
 *
 * @author therepanic
 * @since 1.0.0
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Generates a JWT token for the given user details
     *
     * @param userDetails user details for whom the token is created
     * @return signed JWT token as a string
     */
    public String generateToken(UserDetails userDetails) {
        Date dateNow = new Date();
        Date expirationDate = new Date(dateNow.getTime() + 1000L * 60 * 60 * 24 * 31);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(dateNow)
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Extracts the expiration date from a given JWT token
     *
     * @param token JWT token
     * @return expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Checks if a given JWT token is valid by verifying its signature and structure
     *
     * @param token JWT token
     * @return {@code true} if the token is valid; {@code false} otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Checks if a given JWT token is expired
     *
     * @param token JWT token
     * @return {@code true} if the token is expired; {@code false} otherwise
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the subject (typically user ID) from a given JWT token
     *
     * @param token JWT token
     * @return subject of the token as a string
     */
    public String extractIdFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(bytes, "HmacSHA256");
    }
}
