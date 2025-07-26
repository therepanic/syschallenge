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

package com.syschallenge.company.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.SortField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.syschallenge.company.model.Company;
import com.syschallenge.public_.tables.CompaniesTable;

import lombok.RequiredArgsConstructor;

/**
 * Repository for handling company data persistence operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class CompanyRepository {

	private final DSLContext ctx;

	/**
	 * Saves a new company to the database
	 * @param company entity to save
	 * @return company entity with updated information
	 */
	public Company save(Company company) {
		return this.ctx.insertInto(CompaniesTable.COMPANIES_TABLE)
			.set(CompaniesTable.COMPANIES_TABLE.NAME, company.getName())
			.set(CompaniesTable.COMPANIES_TABLE.SLUG, company.getSlug())
			.set(CompaniesTable.COMPANIES_TABLE.UPDATED_AT, company.getUpdatedAt())
			.returningResult(CompaniesTable.COMPANIES_TABLE)
			.fetchOneInto(Company.class);
	}

	/**
	 * Updates a company to the database
	 * @param company entity to update
	 * @return company entity with updated information
	 */
	public Company update(Company company) {
		return this.ctx.update(CompaniesTable.COMPANIES_TABLE)
			.set(CompaniesTable.COMPANIES_TABLE.NAME, company.getName())
			.set(CompaniesTable.COMPANIES_TABLE.SLUG, company.getSlug())
			.set(CompaniesTable.COMPANIES_TABLE.UPDATED_AT, company.getUpdatedAt())
			.where(CompaniesTable.COMPANIES_TABLE.ID.eq(company.getId()))
			.returningResult(CompaniesTable.COMPANIES_TABLE)
			.fetchOneInto(Company.class);
	}

	/**
	 * Finds all companies with pagination
	 * @param pageable pagination parameters
	 * @return paginated list of companies
	 */
	public Page<Company> findAll(Pageable pageable) {
		List<SortField<LocalDateTime>> orderBy = pageable.getSort()
			.stream()
			.map(order -> order.isAscending()
					? CompaniesTable.COMPANIES_TABLE.field(CompaniesTable.COMPANIES_TABLE.UPDATED_AT).asc()
					: CompaniesTable.COMPANIES_TABLE.field(CompaniesTable.COMPANIES_TABLE.UPDATED_AT).desc())
			.toList();
		List<Company> companies = this.ctx.selectFrom(CompaniesTable.COMPANIES_TABLE)
			.orderBy(orderBy)
			.limit(pageable.getPageSize())
			.offset(pageable.getOffset())
			.fetchInto(Company.class);
		long total = this.ctx.fetchCount(this.ctx.selectFrom(CompaniesTable.COMPANIES_TABLE));
		return new PageImpl<>(companies, pageable, total);
	}

	/**
	 * Finds a company by slug
	 * @param slug slug to search for
	 * @return company entity or null if not found
	 */
	public Company findBySlug(String slug) {
		return this.ctx.selectFrom(CompaniesTable.COMPANIES_TABLE)
			.where(CompaniesTable.COMPANIES_TABLE.SLUG.eq(slug))
			.fetchOneInto(Company.class);
	}

	/**
	 * Finds a company by id
	 * @param id company id to search for
	 * @return company entity or null if not found
	 */
	public Company findById(UUID id) {
		return this.ctx.selectFrom(CompaniesTable.COMPANIES_TABLE)
			.where(CompaniesTable.COMPANIES_TABLE.ID.eq(id))
			.fetchOneInto(Company.class);
	}

	/**
	 * Deletes a company based on the id to the database
	 * @param id company id to search for
	 */
	public void deleteById(UUID id) {
		this.ctx.deleteFrom(CompaniesTable.COMPANIES_TABLE).where(CompaniesTable.COMPANIES_TABLE.ID.eq(id)).execute();
	}

	/**
	 * Checks if a company exists for the given slug
	 * @param slug the slug of the user
	 * @return true if a record exists, false otherwise
	 */
	public boolean existsBySlug(String slug) {
		return this.ctx.fetchExists(this.ctx.selectOne()
			.from(CompaniesTable.COMPANIES_TABLE)
			.where(CompaniesTable.COMPANIES_TABLE.SLUG.eq(slug)));
	}

}
