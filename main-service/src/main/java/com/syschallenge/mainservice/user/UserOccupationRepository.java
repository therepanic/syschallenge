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

package com.syschallenge.mainservice.user;

import com.syschallenge.mainservice.public_.tables.UsersOccupationTable;
import com.syschallenge.mainservice.user.model.UserOccupation;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

/**
 * Repository for handling persistence operations related to users occupations
 *
 * @author panic08
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserOccupationRepository {

    private final DSLContext ctx;

    /**
     * Saves a new user occupation to the database
     *
     * @param userOccupation user occupation entity to save
     * @return user occupation entity with updated information
     */
    public UserOccupation save(UserOccupation userOccupation) {
        return ctx.insertInto(UsersOccupationTable.USERS_OCCUPATION_TABLE)
                .set(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID, userOccupation.getUserId())
                .set(UsersOccupationTable.USERS_OCCUPATION_TABLE.COMPANY, userOccupation.getCompany())
                .set(UsersOccupationTable.USERS_OCCUPATION_TABLE.TITLE, userOccupation.getTitle())
                .set(UsersOccupationTable.USERS_OCCUPATION_TABLE.START_DATE, userOccupation.getStartDate())
                .set(UsersOccupationTable.USERS_OCCUPATION_TABLE.END_DATE, userOccupation.getEndDate())
                .returningResult(UsersOccupationTable.USERS_OCCUPATION_TABLE)
                .fetchOneInto(UserOccupation.class);
    }
}
