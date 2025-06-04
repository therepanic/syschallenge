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

package com.syschallenge.company.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.syschallenge.company.dto.CompanyDto;
import com.syschallenge.company.model.Company;

/**
 * Mapper for converting Company entity to CompanyDto
 *
 * @author therepanic
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CompanyToCompanyDtoMapper {

    /**
     * Converts Company entity to CompanyDto
     *
     * @param company the company entity to convert
     * @return the corresponding CompanyDto
     */
    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "slug", source = "slug"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "updatedAt", source = "updatedAt")
    })
    CompanyDto companyToCompanyDto(Company company);
}
