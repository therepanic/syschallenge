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

package com.syschallenge.auth;

import com.syschallenge.auth.model.Me;
import com.syschallenge.auth.payload.response.AuthResponse;
import com.syschallenge.oauth.OAuthProviderFactory;
import com.syschallenge.oauth.OAuthType;
import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.shared.security.UserDetails;
import com.syschallenge.shared.security.jwt.JwtUtil;
import com.syschallenge.user.repository.UserBasicInfoRepository;
import com.syschallenge.user.repository.UserLinkedSocialRepository;
import com.syschallenge.user.repository.UserRepository;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserBasicInfo;
import com.syschallenge.user.model.UserLinkedSocial;
import com.syschallenge.user.model.UserLinkedSocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for handling authentication-related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthProviderFactory providerFactory;
    private final UserRepository userRepository;
    private final UserBasicInfoRepository userBasicInfoRepository;
    private final UserLinkedSocialRepository userLinkedSocialRepository;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user through OAuth using an authorization code
     *
     * @param type social type for OAuth
     * @param code authorization code provided by OAuth
     * @return response containing the authentication response with the JWT token
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public AuthResponse authBySocial(OAuthType type, String code) {
        OAuthUserInfo userInfo = providerFactory.getProvider(type).extractUser(code);

        if (userLinkedSocialRepository.existsByVerification(userInfo.providerUserId())) {
            User currentUser = userRepository.findById(
                    userLinkedSocialRepository.findUserIdByVerification(userInfo.providerUserId())
            );
            String jwtToken = jwtUtil.generateToken(new UserDetails(currentUser.getId()));
            return new AuthResponse(jwtToken);
        } else {
            User newUser = userRepository.save(
                    User.builder()
                            .email(userInfo.email())
                            .username(userInfo.username())
                            .registeredAt(LocalDateTime.now())
                            .build()
            );
            userBasicInfoRepository.save(
                    UserBasicInfo.builder()
                            .userId(newUser.getId())
                            .name(newUser.getUsername())
                            .build()
            );
            userLinkedSocialRepository.save(
                    UserLinkedSocial.builder()
                            .userId(newUser.getId())
                            .type(UserLinkedSocialType.valueOf(type.name()))
                            .verification(userInfo.providerUserId())
                            .build()
            );
            String jwtToken = jwtUtil.generateToken(new UserDetails(newUser.getId()));
            return new AuthResponse(jwtToken);
        }
    }

    public Me me(UUID principalUserId) {
        return new Me(principalUserId, userRepository.findUsernameById(principalUserId), userBasicInfoRepository.findNameByUserId(principalUserId));
    }
}
