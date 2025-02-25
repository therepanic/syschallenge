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

package com.syschallenge.company.controller;

import com.syschallenge.company.dto.CompanyDto;
import com.syschallenge.company.payload.request.CreateCompanyRequest;
import com.syschallenge.company.payload.request.UpdateCompanyRequest;
import com.syschallenge.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

/**
 * REST controller for handling company-related operations
 *
 * @author panic08
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Endpoint for retrieving a paginated list of companies
     *
     * @param page the page number (default is 0)
     * @param size the number of items per page (default is 20)
     * @param sort the sorting order (default is descending)
     * @return a paginated list of companies
     */
    @GetMapping("/all")
    public Page<CompanyDto> getAll(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(defaultValue = "desc") String sort) {
        return companyService.getAll(page, size, sort);
    }

    /**
     * Endpoint for retrieving company information by ID
     *
     * @param id the UUID of the company
     * @return the retrieved company DTO with persisted data
     */
    @GetMapping("/{id}")
    public CompanyDto get(@PathVariable("id") UUID id) {
        return companyService.get(id);
    }

    /**
     * Endpoint for retrieving company information by slug
     *
     * @param slug the slug of the company
     * @return the retrieved company DTO with persisted data
     */
    @GetMapping
    public CompanyDto getBySlug(@RequestParam("slug") String slug) {
        return companyService.getBySlug(slug);
    }

    /**
     * Endpoint for creating a new company
     *
     * @param request the validated create company request payload
     * @return the created company DTO with persisted data
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyDto create(@RequestBody CreateCompanyRequest request) {
        return companyService.create(request);
    }

    /**
     * Endpoint for updating company information
     *
     * @param id the UUID of the company to update
     * @param request the validated update company request payload
     * @return the updated company DTO with persisted data
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CompanyDto update(@PathVariable("id") UUID id, @RequestBody UpdateCompanyRequest request) {
        return companyService.update(id, request);
    }

    /**
     * Endpoint for deleting a company
     *
     * @param id the UUID of the company to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable("id") UUID id) {
        companyService.delete(id);
    }

}
