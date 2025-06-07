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

package com.syschallenge.topic.controller;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.syschallenge.topic.dto.TopicDto;
import com.syschallenge.topic.payload.CreateTopicRequest;
import com.syschallenge.topic.payload.UpdateTopicRequest;
import com.syschallenge.topic.service.TopicService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling topic-related operations
 *
 * @author therepanic
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    /**
     * Endpoint for retrieving a collection of topics
     *
     * @return collection of topics
     */
    @GetMapping("/all")
    public Collection<TopicDto> getAll() {
        return this.topicService.getAll();
    }

    /**
     * Endpoint for retrieving topic information by ID
     *
     * @param id the UUID of the topic
     * @return the retrieved topic DTO with persisted data
     */
    @GetMapping("/{id}")
    public TopicDto get(@PathVariable("id") UUID id) {
        return this.topicService.get(id);
    }

    /**
     * Endpoint for creating a new topic
     *
     * @param request the validated create topic request payload
     * @return the created topic DTO with persisted data
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public TopicDto create(@RequestBody CreateTopicRequest request) {
        return this.topicService.create(request);
    }

    /**
     * Endpoint for updating topic information
     *
     * @param id the UUID of the topic to update
     * @param request the validated update topic request payload
     * @return the updated topic DTO with persisted data
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TopicDto update(@PathVariable("id") UUID id, @RequestBody UpdateTopicRequest request) {
        return this.topicService.update(id, request);
    }

    /**
     * Endpoint for deleting a topic
     *
     * @param id the UUID of the topic to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable("id") UUID id) {
        this.topicService.delete(id);
    }
}
