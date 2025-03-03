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

package com.syschallenge.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.syschallenge.company.dto.CompanyDto;
import com.syschallenge.company.exception.CompanyAlreadyExistsException;
import com.syschallenge.company.mapper.CompanyToCompanyDtoMapper;
import com.syschallenge.company.model.Company;
import com.syschallenge.company.payload.request.CreateCompanyRequest;
import com.syschallenge.company.payload.request.UpdateCompanyRequest;
import com.syschallenge.company.repository.CompanyRepository;
import com.syschallenge.company.service.CompanyService;

/**
 * @author panic08
 * @since 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @Mock private CompanyRepository companyRepository;

    @Mock private CompanyToCompanyDtoMapper companyToCompanyDtoMapper;

    @InjectMocks private CompanyService companyService;

    @Test
    void getAll_returnsPageOfCompanyDtos() {
        // given
        int page = 0;
        int size = 5;
        String sort = "ASC";
        List<Company> companies =
                List.of(
                        Company.builder()
                                .id(UUID.randomUUID())
                                .name("Test Co")
                                .slug("test-co")
                                .updatedAt(LocalDateTime.now())
                                .build());
        Page<Company> companyPage = new PageImpl<>(companies);
        PageRequest pageRequest =
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort), "updatedAt"));
        when(companyRepository.findAll(pageRequest)).thenReturn(companyPage);
        when(companyToCompanyDtoMapper.companyToCompanyDto(any(Company.class)))
                .thenReturn(
                        new CompanyDto(
                                UUID.randomUUID(), "test-co", "Test Co", LocalDateTime.now()));

        // when
        Page<CompanyDto> result = companyService.getAll(page, size, sort);

        // then
        assertNotNull(result);
        assertEquals(companies.size(), result.getContent().size());
        verify(companyRepository).findAll(pageRequest);
    }

    @Test
    void get_returnsCompanyDto() {
        // given
        UUID id = UUID.randomUUID();
        Company company =
                Company.builder()
                        .id(id)
                        .name("Test Co")
                        .slug("test-co")
                        .updatedAt(LocalDateTime.now())
                        .build();
        CompanyDto dto = new CompanyDto(id, "test-co", "Test Co", company.getUpdatedAt());
        when(companyRepository.findById(id)).thenReturn(company);
        when(companyToCompanyDtoMapper.companyToCompanyDto(company)).thenReturn(dto);

        // when
        CompanyDto result = companyService.get(id);

        // then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(companyRepository).findById(id);
    }

    @Test
    void getBySlug_returnsCompanyDto() {
        // given
        String slug = "test-co";
        Company company =
                Company.builder()
                        .id(UUID.randomUUID())
                        .name("Test Co")
                        .slug(slug)
                        .updatedAt(LocalDateTime.now())
                        .build();
        CompanyDto dto = new CompanyDto(company.getId(), slug, "Test Co", company.getUpdatedAt());
        when(companyRepository.findBySlug(slug)).thenReturn(company);
        when(companyToCompanyDtoMapper.companyToCompanyDto(company)).thenReturn(dto);

        // when
        CompanyDto result = companyService.getBySlug(slug);

        // then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(companyRepository).findBySlug(slug);
    }

    @Test
    void create_throwsCompanyAlreadyExistsException_whenSlugAlreadyExists() {
        // given
        CreateCompanyRequest request = new CreateCompanyRequest("test-co", "Test Co");
        when(companyRepository.existsBySlug(request.slug())).thenReturn(true);

        // when & then
        CompanyAlreadyExistsException exception =
                assertThrows(
                        CompanyAlreadyExistsException.class, () -> companyService.create(request));
        assertEquals(
                "Company with slug '" + request.slug() + "' already exists",
                exception.getMessage());
    }

    @Test
    void create_returnsCompanyDto_whenCompanyDoesNotExist() {
        // given
        CreateCompanyRequest request = new CreateCompanyRequest("new-co", "New Co");
        Company companyToSave = Company.builder().slug(request.slug()).name(request.name()).build();
        Company savedCompany =
                Company.builder()
                        .id(UUID.randomUUID())
                        .slug(request.slug())
                        .name(request.name())
                        .updatedAt(LocalDateTime.now())
                        .build();
        CompanyDto dto =
                new CompanyDto(
                        savedCompany.getId(),
                        request.slug(),
                        request.name(),
                        savedCompany.getUpdatedAt());
        when(companyRepository.existsBySlug(request.slug())).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);
        when(companyToCompanyDtoMapper.companyToCompanyDto(savedCompany)).thenReturn(dto);

        // when
        CompanyDto result = companyService.create(request);

        // then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void update_returnsCompanyDto() {
        // given
        UUID id = UUID.randomUUID();
        UpdateCompanyRequest request = new UpdateCompanyRequest("updated-co", "Updated Co");
        Company updatedCompany =
                Company.builder()
                        .id(id)
                        .slug(request.slug())
                        .name(request.name())
                        .updatedAt(LocalDateTime.now())
                        .build();
        CompanyDto dto =
                new CompanyDto(id, request.slug(), request.name(), updatedCompany.getUpdatedAt());
        when(companyRepository.update(any(Company.class))).thenReturn(updatedCompany);
        when(companyToCompanyDtoMapper.companyToCompanyDto(updatedCompany)).thenReturn(dto);

        // when
        CompanyDto result = companyService.update(id, request);

        // then
        assertNotNull(result);
        assertEquals(dto, result);
        verify(companyRepository).update(any(Company.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        companyService.delete(id);

        // then
        verify(companyRepository).deleteById(id);
    }
}
