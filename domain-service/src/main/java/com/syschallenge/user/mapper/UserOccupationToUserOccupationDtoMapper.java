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

package com.syschallenge.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.syschallenge.user.dto.UserOccupationDto;
import com.syschallenge.user.model.UserOccupation;

/**
 * Mapper for converting UserOccupation entity to UserOccupationDto
 *
 * @author therepanic
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface UserOccupationToUserOccupationDtoMapper {

	/**
	 * Converts UserOccupation entity to UserOccupationDto
	 * @param userOccupation the user occupation entity to convert
	 * @return the corresponding UserOccupationDto
	 */
	@Mappings({ @Mapping(target = "userId", source = "userId"), @Mapping(target = "company", source = "company"),
			@Mapping(target = "title", source = "title"), @Mapping(target = "startDate", source = "startDate"),
			@Mapping(target = "endDate", source = "endDate"), })
	UserOccupationDto userOccupationToUserOccupationDto(UserOccupation userOccupation);

}
