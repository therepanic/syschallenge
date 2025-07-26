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

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.syschallenge.public_.tables.UsersOccupationTable;
import com.syschallenge.user.model.UserOccupation;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling persistence operations related to users occupations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserOccupationRepository {

	private final DSLContext ctx;

	/**
	 * Saves a new user occupation to the database
	 * @param userOccupation user occupation entity to save
	 * @return user occupation entity with updated information
	 */
	public UserOccupation save(UserOccupation userOccupation) {
		return this.ctx.insertInto(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID, userOccupation.getUserId())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.COMPANY, userOccupation.getCompany())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.TITLE, userOccupation.getTitle())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.START_DATE, userOccupation.getStartDate())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.END_DATE, userOccupation.getEndDate())
			.returningResult(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.fetchOneInto(UserOccupation.class);
	}

	/**
	 * Finds a user occupation by user ID
	 * @param userId user ID to search for
	 * @return user occupation entity or null if not found
	 */
	public UserOccupation findByUserId(UUID userId) {
		return this.ctx.selectFrom(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.where(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID.eq(userId))
			.fetchOneInto(UserOccupation.class);
	}

	/**
	 * Updates a user occupation based on the user id to the database
	 * @param userOccupation user occupation entity to update
	 * @return user occupation entity with updated information
	 */
	public UserOccupation updateByUserId(UserOccupation userOccupation) {
		return this.ctx.update(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.COMPANY, userOccupation.getCompany())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.TITLE, userOccupation.getTitle())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.START_DATE, userOccupation.getStartDate())
			.set(UsersOccupationTable.USERS_OCCUPATION_TABLE.END_DATE, userOccupation.getEndDate())
			.where(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID.eq(userOccupation.getUserId()))
			.returningResult(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.fetchOneInto(UserOccupation.class);
	}

	/**
	 * Deletes a user occupation based on the user id to the database
	 * @param userId the ID of the user
	 */
	public void deleteByUserId(UUID userId) {
		this.ctx.deleteFrom(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.where(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID.eq(userId))
			.execute();
	}

	/**
	 * Checks if a user occupation exists for the given user ID.
	 * @param userId the ID of the user
	 * @return true if a record exists, false otherwise
	 */
	public boolean existsByUserId(UUID userId) {
		return this.ctx.fetchExists(this.ctx.selectOne()
			.from(UsersOccupationTable.USERS_OCCUPATION_TABLE)
			.where(UsersOccupationTable.USERS_OCCUPATION_TABLE.USER_ID.eq(userId)));
	}

}
