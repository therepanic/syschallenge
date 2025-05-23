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

package com.syschallenge.company.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.syschallenge.company.dto.CompanyDto;
import com.syschallenge.company.exception.CompanyAlreadyExistsException;
import com.syschallenge.company.mapper.CompanyToCompanyDtoMapper;
import com.syschallenge.company.model.Company;
import com.syschallenge.company.payload.request.CreateCompanyRequest;
import com.syschallenge.company.payload.request.UpdateCompanyRequest;
import com.syschallenge.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service for handling company-related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyToCompanyDtoMapper companyToCompanyDtoMapper;

    /**
     * Get paginated list of companies
     *
     * @param page the page number
     * @param size the number of items per page
     * @return a paginated list of company DTOs
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Page<CompanyDto> getAll(int page, int size, String sort) {
        return this.companyRepository
                .findAll(
                        PageRequest.of(
                                page, size, Sort.by(Sort.Direction.fromString(sort), "updatedAt")))
                .map(this.companyToCompanyDtoMapper::companyToCompanyDto);
    }

    /**
     * Get a company details
     *
     * @param id the company id whose company is being got
     * @return a DTO containing the got company information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompanyDto get(UUID id) {
        return this.companyToCompanyDtoMapper.companyToCompanyDto(
                this.companyRepository.findById(id));
    }

    /**
     * Get a company details by slug
     *
     * @param slug the company slug whose company is being got
     * @return a DTO containing the got company information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompanyDto getBySlug(String slug) {
        return this.companyToCompanyDtoMapper.companyToCompanyDto(
                this.companyRepository.findBySlug(slug));
    }

    /**
     * Creates a company
     *
     * @param request the company create details
     * @return a DTO containing the created company information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompanyDto create(CreateCompanyRequest request) {
        if (this.companyRepository.existsBySlug(request.slug())) {
            throw new CompanyAlreadyExistsException(
                    "Company with slug '" + request.slug() + "' already exists");
        }
        return this.companyToCompanyDtoMapper.companyToCompanyDto(
                this.companyRepository.save(
                        Company.builder()
                                .slug(request.slug())
                                .name(request.name())
                                .updatedAt(LocalDateTime.now())
                                .build()));
    }

    /**
     * Updates a company
     *
     * @param id the company ID to update
     * @param request the request containing updated company details
     * @return a DTO containing the updated company information
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CompanyDto update(UUID id, UpdateCompanyRequest request) {
        return this.companyToCompanyDtoMapper.companyToCompanyDto(
                this.companyRepository.update(
                        Company.builder()
                                .id(id)
                                .name(request.name())
                                .slug(request.slug())
                                .updatedAt(LocalDateTime.now())
                                .build()));
    }

    /**
     * Deletes a company details
     *
     * @param id the company ID whose company is being deleted
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(UUID id) {
        this.companyRepository.deleteById(id);
    }
}
