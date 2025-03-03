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

import java.util.Objects;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.syschallenge.public_.tables.UsersBasicInfoTable;
import com.syschallenge.user.model.UserBasicInfo;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling persistence operations related to users basic information
 *
 * @author panic08
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserBasicInfoRepository {

    private final DSLContext ctx;

    /**
     * Saves a new user basic info to the database
     *
     * @param userBasicInfo user basic info entity to save
     * @return user basic info entity with updated information
     */
    public UserBasicInfo save(UserBasicInfo userBasicInfo) {
        return ctx.insertInto(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE)
                .set(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.USER_ID, userBasicInfo.getUserId())
                .set(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.NAME, userBasicInfo.getName())
                .set(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.SUMMARY, userBasicInfo.getSummary())
                .set(
                        UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.BIRTHDAY,
                        userBasicInfo.getBirthday())
                .set(
                        UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.GENDER,
                        Objects.toString(userBasicInfo.getGender(), null))
                .returningResult(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE)
                .fetchOneInto(UserBasicInfo.class);
    }

    /**
     * Finds a name by their userId
     *
     * @param userId UUID of the user to find
     * @return name associated with the given userId
     */
    public String findNameByUserId(UUID userId) {
        return ctx.select(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.NAME)
                .from(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE)
                .where(UsersBasicInfoTable.USERS_BASIC_INFO_TABLE.USER_ID.eq(userId))
                .fetchOneInto(String.class);
    }
}
