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

import com.syschallenge.public_.tables.UsersPhotoTable;
import com.syschallenge.user.model.UserPhoto;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling user photo data persistence operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class UserPhotoRepository {

	private final DSLContext ctx;

	/**
	 * Finds the objectKey associated with user id
	 * @param userId user id to search for
	 * @return objectKey of the user photo associated with the given userId
	 */
	public String findObjectKeyByUserId(UUID userId) {
		return this.ctx.select(UsersPhotoTable.USERS_PHOTO_TABLE.OBJECT_KEY)
			.from(UsersPhotoTable.USERS_PHOTO_TABLE)
			.where(UsersPhotoTable.USERS_PHOTO_TABLE.USER_ID.eq(userId))
			.fetchOneInto(String.class);
	}

	/**
	 * Updates a user photo objectKey based on the user id
	 * @param userId user id to update objectKey
	 * @return updated user photo id objectKey
	 */
	public String updateObjectKeyByUserId(String objectKey, UUID userId) {
		return this.ctx.update(UsersPhotoTable.USERS_PHOTO_TABLE)
			.set(UsersPhotoTable.USERS_PHOTO_TABLE.OBJECT_KEY, objectKey)
			.where(UsersPhotoTable.USERS_PHOTO_TABLE.USER_ID.eq(userId))
			.returningResult(UsersPhotoTable.USERS_PHOTO_TABLE)
			.fetchOneInto(String.class);
	}

	/**
	 * Saves a new user photo
	 * @param userPhoto user linked social entity to save
	 * @return user photo entity with updated information
	 */
	public UserPhoto save(UserPhoto userPhoto) {
		return this.ctx.insertInto(UsersPhotoTable.USERS_PHOTO_TABLE)
			.set(UsersPhotoTable.USERS_PHOTO_TABLE.USER_ID, userPhoto.getUserId())
			.set(UsersPhotoTable.USERS_PHOTO_TABLE.OBJECT_KEY, userPhoto.getObjectKey())
			.returningResult(UsersPhotoTable.USERS_PHOTO_TABLE)
			.fetchOneInto(UserPhoto.class);
	}

	/**
	 * Checks if a user photo exists for the given user id.
	 * @param userId the ID of the user
	 * @return true if a record exists, false otherwise
	 */
	public boolean existsByUserId(UUID userId) {
		return this.ctx.fetchExists(this.ctx.selectOne()
			.from(UsersPhotoTable.USERS_PHOTO_TABLE)
			.where(UsersPhotoTable.USERS_PHOTO_TABLE.USER_ID.eq(userId)));
	}

}
