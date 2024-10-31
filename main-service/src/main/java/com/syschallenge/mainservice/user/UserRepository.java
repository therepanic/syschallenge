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

import com.syschallenge.mainservice.public_.tables.UsersTable;
import com.syschallenge.mainservice.user.model.User;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for handling user data persistence operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DSLContext ctx;

    /**
     * Saves a new user to the database
     *
     * @param user user entity to save
     * @return user entity with updated information
     */
    public User save(User user) {
        return ctx.insertInto(UsersTable.USERS_TABLE)
                .set(UsersTable.USERS_TABLE.EMAIL, user.getEmail())
                .set(UsersTable.USERS_TABLE.PASSWORD, user.getPassword())
                .set(UsersTable.USERS_TABLE.REGISTERED_AT, user.getRegisteredAt())
                .returningResult(UsersTable.USERS_TABLE)
                .fetchOneInto(User.class);
    }

    /**
     * Finds a user by their id
     *
     * @param id UUID of the user to find
     * @return user entity associated with the given id
     */
    public User findById(UUID id) {
        return ctx.selectFrom(UsersTable.USERS_TABLE)
                .where(UsersTable.USERS_TABLE.ID.eq(id))
                .fetchOneInto(User.class);
    }
}
