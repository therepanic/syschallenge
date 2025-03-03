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

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.syschallenge.oauth.OAuthUserInfo;
import com.syschallenge.user.model.User;
import com.syschallenge.user.model.UserBasicInfo;
import com.syschallenge.user.model.UserRole;
import com.syschallenge.user.repository.UserBasicInfoRepository;
import com.syschallenge.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for handling user related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBasicInfoRepository userBasicInfoRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User create(OAuthUserInfo userInfo) {
        User newUser =
                User.builder()
                        .email(userInfo.email())
                        .username(userInfo.username())
                        .role(UserRole.DEFAULT)
                        .registeredAt(LocalDateTime.now())
                        .build();
        userRepository.save(newUser);
        userBasicInfoRepository.save(
                UserBasicInfo.builder()
                        .userId(newUser.getId())
                        .name(newUser.getUsername())
                        .build());
        return newUser;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String getUsernameById(UUID id) {
        return userRepository.findUsernameById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User getById(UUID id) {
        return userRepository.findById(id);
    }
}
