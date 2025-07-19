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

package com.syschallenge.user.repository;

import java.util.UUID;

import com.syschallenge.public_.enums.UserLinkedSocialType;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.syschallenge.public_.tables.UsersLinkedSocialTable;
import com.syschallenge.user.model.UserLinkedSocial;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling persistence operations related to user linked social accounts
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserLinkedSocialRepository {

    private final DSLContext ctx;

    /**
     * Saves a new user linked social to the database
     *
     * @param userLinkedSocial user linked social entity to save
     * @return user linked social entity with updated information
     */
    public UserLinkedSocial save(UserLinkedSocial userLinkedSocial) {
        return this.ctx
                .insertInto(UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE)
                .set(
                        UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.USER_ID,
                        userLinkedSocial.getUserId())
                .set(
                        UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.TYPE,
                        UserLinkedSocialType.valueOf(userLinkedSocial.getType().name()))
                .set(
                        UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.VERIFICATION,
                        userLinkedSocial.getVerification())
                .returningResult(UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE)
                .fetchOneInto(UserLinkedSocial.class);
    }

    /**
     * Checks if a user linked social account exists by its verification token
     *
     * @param verification verification token to check
     * @return {@code true} if an account with the given verification token exists, {@code false}
     *     otherwise
     */
    public boolean existsByVerification(String verification) {
        return this.ctx.fetchExists(
                this.ctx
                        .selectFrom(UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE)
                        .where(
                                UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.VERIFICATION.eq(
                                        verification)));
    }

    /**
     * Finds the user id associated with a specific verification token
     *
     * @param verification verification token to search for
     * @return UUID of the user associated with the given verification token
     */
    public UUID findUserIdByVerification(String verification) {
        return this.ctx
                .select(UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.USER_ID)
                .from(UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE)
                .where(
                        UsersLinkedSocialTable.USERS_LINKED_SOCIAL_TABLE.VERIFICATION.eq(
                                verification))
                .fetchOneInto(UUID.class);
    }
}
