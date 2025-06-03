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

package com.syschallenge.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.syschallenge.oauth.OAuthType;
import com.syschallenge.user.model.UserLinkedSocial;
import com.syschallenge.user.model.UserLinkedSocialType;
import com.syschallenge.user.repository.UserLinkedSocialRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service for handling user linked social related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserLinkedSocialService {

    private final UserLinkedSocialRepository userLinkedSocialRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserLinkedSocial create(UUID userId, OAuthType type, String verification) {
        return this.userLinkedSocialRepository.save(
                UserLinkedSocial.builder()
                        .userId(userId)
                        .type(UserLinkedSocialType.valueOf(type.name()))
                        .verification(verification)
                        .build());
    }

    public UUID getUserIdByVerification(String verification) {
        return this.userLinkedSocialRepository.findUserIdByVerification(verification);
    }

    public boolean existsByVerification(String verification) {
        return this.userLinkedSocialRepository.existsByVerification(verification);
    }
}
